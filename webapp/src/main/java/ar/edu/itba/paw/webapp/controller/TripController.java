package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.webapp.form.EditTripForm;
import ar.edu.itba.paw.webapp.form.TripCreateForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;
import se.walkercrou.places.exception.GooglePlacesException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class TripController extends MainController{

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    private GooglePlaces client = new GooglePlaces("AIzaSyDf5BlyQV8TN06oWY_U7Z_MnqWjIci2k2M");

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private static final int MAX_TRIPS_PAGE = 4;
    private static final long MAX_UPLOAD_SIZE = 5242880;

    @Autowired
    ActivityService as;
    @Autowired
    TripService ts;
    @Autowired
    UserService us;
    @Autowired
    PlaceService ps;
    @Autowired
    TripPicturesService tripPictureService;

    @RequestMapping("/home/create-trip")
    public ModelAndView createTripGet(@ModelAttribute("createTripForm") final TripCreateForm form) {
        return new ModelAndView("createTrip");
    }

    @RequestMapping("/home/trips/{pageNum}")
    public ModelAndView getUserTrips(@ModelAttribute("user") User user,  @PathVariable(value = "pageNum") int pageNum) {
        ModelAndView mav = new ModelAndView("userTrips");
        Optional<User> userOptional = us.findByid(user.getId());
        if(!userOptional.isPresent()) {
            return mav;
        }
        User u = userOptional.get();
        int userTripsQty = u.getTrips().size();
        int requiredPages = (int) Math.ceil(userTripsQty/(double)MAX_TRIPS_PAGE);
        if(pageNum > 1 && pageNum > requiredPages) {
            mav.setViewName("404");
            return mav;
        }
        int n = (pageNum - 1) * MAX_TRIPS_PAGE;

        List<Trip> userTrips =  u.getTrips();
        System.out.println(userTrips.size());
        int size = userTrips.size();
        if(size > MAX_TRIPS_PAGE) {
            if(n > size) {
                int end = ((n + MAX_TRIPS_PAGE) > size) ? size-1 : (n + MAX_TRIPS_PAGE);
                userTrips = userTrips.subList(n, end);
            }
        }
        List<DataPair<Trip, DataPair<ar.edu.itba.paw.model.Place, Boolean>>> dataPairList = new LinkedList<>();
        for (Trip trip: userTrips) {
            long placeId = trip.getStartPlaceId();
            ar.edu.itba.paw.model.Place place = ps.findById(placeId).get();
            dataPairList.add(new DataPair<>(trip,
                    new DataPair<>(place,tripPictureService.findByTripId(trip.getId()).isPresent())));
        }
        int pageQty = (int)Math.round(userTripsQty / (double) MAX_TRIPS_PAGE);
        mav.addObject("pageQty", pageQty);
        mav.addObject("isEmpty", dataPairList.isEmpty());
        mav.addObject("userTripsList", dataPairList);
        mav.addObject("dateFormat", dateFormat);
        return mav;
    }


    @RequestMapping(value = "/home/create-trip", method = {RequestMethod.POST})
    public ModelAndView createTripPost(@ModelAttribute("user") User user,
                                       @Valid @ModelAttribute("createTripForm") final TripCreateForm form,
                                       final BindingResult errors) {
        List<Place> places;
        ar.edu.itba.paw.model.Place modelPlace;
        ModelAndView mav = new ModelAndView("createTrip");
        if(errors.hasErrors()) return mav;
        LOGGER.debug("NO ERRORS IN CREATE TRIP FORM");
        try {
            places = client.getPlacesByQuery(form.getPlaceInput(), GooglePlaces.MAXIMUM_RESULTS);
        }
        catch(GooglePlacesException gpe) {
            LOGGER.debug("INVALID GOOGLE PLACES QUERY LOCATION");
            mav.addObject("invalidPlace", true);
            return mav;
        }
        Place place = places.get(0);
        LOGGER.debug("Google Place name is {}", place.getName());

        Optional<ar.edu.itba.paw.model.Place> maybePlace = ps.findByGoogleId(place.getPlaceId());
        modelPlace = maybePlace.orElseGet(() -> ps.create(place.getPlaceId(), place.getName(), place.getLatitude(),
                place.getLongitude(), place.getAddress()));
        Trip trip = ts.create(user.getId(), modelPlace.getId(), form.getName(), form.getDescription(),
                DateManipulation.stringToCalendar(form.getStartDate()),
                DateManipulation.stringToCalendar(form.getEndDate()));
        if(trip.getPlaces() != null) {
            trip.getPlaces().add(modelPlace);
        }
        else {
            trip.setPlaces(new LinkedList<>());
        }
        if(trip.getUsers() != null) {
            trip.getUsers().add(user);
        }
        else {
            trip.setUsers(new LinkedList<>());
        }
        user.getTrips().add(trip);
        String redirectFormat = String.format("redirect:/home/trip/%d", trip.getId());
        mav.setViewName(redirectFormat);
        return mav;
    }

    @RequestMapping(value = "/home/trip/{tripId}", method = {RequestMethod.GET})
    public ModelAndView trip(@ModelAttribute("user") User user, @PathVariable(value = "tripId") long tripId,
                             @ModelAttribute("editTripForm") final EditTripForm form) {
        ModelAndView mav = new ModelAndView("trip");
        Optional<Trip> maybeTrip = ts.findById(tripId);
        Trip trip = maybeTrip.get();
        List<DataPair<Activity, ar.edu.itba.paw.model.Place>> tripActAndPlace = as.getTripActivitiesDetails(trip);
        mav.addObject("hasTripPicture", tripPictureService.findByTripId(tripId).isPresent());
        mav.addObject("isEmpty", tripActAndPlace.size() == 0);
        mav.addObject("isTravelling", trip.getUsers().contains(user));
        //mav.addObject("isAdmin", trip.getCreatedBy().getId() == user.getId());
        mav.addObject("places", trip.getPlaces());
        mav.addObject("users", trip.getUsers());
        mav.addObject("actAndPlaces", tripActAndPlace);
        mav.addObject("trip", trip);
        mav.addObject("startDate", trip.getStartDate().getTime());
        mav.addObject("endDate", trip.getEndDate().getTime());
        return mav;
    }

    @RequestMapping(value = "/home/trip/{tripId}", method = {RequestMethod.POST})
    public ModelAndView editTrip(@ModelAttribute("user") User user, @PathVariable(value = "tripId") long tripId,
                             @Valid @ModelAttribute("editTripForm") final EditTripForm form, BindingResult errors,
                                 final RedirectAttributes redirectAttributes) {
        String redirectFormat = String.format("redirect:/home/trip/%d", tripId);
        ModelAndView mav = new ModelAndView(redirectFormat);
        if(errors.hasErrors()) {
            return mav;
        }
        MultipartFile tripPicture = form.getImageUpload();
        byte[] imageBytes;
        if(tripPicture != null && !tripPicture.isEmpty()) {
            String contentType = tripPicture.getContentType();
            if(!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                redirectAttributes.addFlashAttribute("invalidContentError", true);
                System.out.println("invalid content error");
                return mav;
            }
            else if(tripPicture.getSize() > MAX_UPLOAD_SIZE) {
                redirectAttributes.addFlashAttribute("fileSizeError", true);
                System.out.println("file size error");
                return mav;
            }
            try {
                imageBytes = tripPicture.getBytes();
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("generalError", true);
                System.out.println("general error 1");
                return mav;
            }
        }
        else {
            redirectAttributes.addFlashAttribute("generalError", true);
            System.out.println("general error 2");
            return mav;
        }
        if(tripPictureService.findByTripId(tripId).isPresent()) {
            if(!tripPictureService.deleteByTripId(tripId)) {
                mav.addObject("generalError", true);
                return mav;
            }
        }
        Optional<Trip> tripOptional = ts.findById(tripId);
        if(tripOptional.isPresent()) {
            TripPicture tripPictureModel = tripPictureService.create(tripOptional.get(), imageBytes);
            tripOptional.get().setProfilePicture(tripPictureModel);
        }
        return mav;
    }


    @RequestMapping("/home/trip/{tripId}/join")
    public ModelAndView joinTrip(@ModelAttribute("user") User user, @PathVariable(value = "tripId") long tripId) {
        ModelAndView mav = new ModelAndView("trip");
        Optional<User> userOptional = us.findByid(user.getId());
        Optional<Trip> tripOptional = ts.findById(tripId);
        tripOptional.ifPresent(trip -> trip.getUsers().add(user));
        userOptional.ifPresent(value -> value.getTrips().add(tripOptional.get()));
        String redirect = String.format("redirect:/home/trip/%d", tripId);
        mav.setViewName(redirect);
        return mav;
    }

    @RequestMapping("/home/search-trip/")
    public ModelAndView search(@ModelAttribute("user") User user, @RequestParam(value = "nameInput") String nameInput) {
        ModelAndView mav = new ModelAndView("searchTrips");
        List<Trip> trips = ts.findByName(nameInput);
        mav.addObject("tripQty", trips.size());
        mav.addObject("tripsList", trips);
        return mav;
    }

    @RequestMapping(value = "/home/trip/{tripId}/image", method = {RequestMethod.GET})
    public void getProfileImage(@PathVariable(value = "tripId") long tripId, HttpServletResponse response) {
        Optional<TripPicture> tripPictureOptional = tripPictureService.findByTripId(tripId);
        try {
            if(tripPictureOptional.isPresent()) {
                TripPicture tp = tripPictureOptional.get();
                String mimeType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(tp.getPicture()));
                response.setContentType(mimeType);
                response.getOutputStream().write(tp.getPicture());
            }
        }
        catch (IOException ex) {
            //nothing to do here
        }
    }

}