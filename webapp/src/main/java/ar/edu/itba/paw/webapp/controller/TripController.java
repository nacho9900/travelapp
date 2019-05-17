package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.webapp.form.TripCreateForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;
import se.walkercrou.places.exception.GooglePlacesException;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class TripController extends MainController{

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    private GooglePlaces client = new GooglePlaces("AIzaSyDf5BlyQV8TN06oWY_U7Z_MnqWjIci2k2M");

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    ActivityService as;
    @Autowired
    TripService ts;
    @Autowired
    UserService us;
    @Autowired
    PlaceService ps;
    @Autowired
    TripUsersService tus;
    @Autowired
    TripPlacesService tps;

    @RequestMapping("/home/create-trip")
    public ModelAndView createTripGet(@ModelAttribute("createTripForm") final TripCreateForm form) {
        return new ModelAndView("createTrip");
    }

    @RequestMapping("/home/trips")
    public ModelAndView getUserTrips(@ModelAttribute("user") User user) {
        ModelAndView mav = new ModelAndView("userTrips");
        List<Trip> userTrips = ts.findUserTrips(user.getId());
        List<DataPair<Trip, ar.edu.itba.paw.model.Place>> dataPairList = new LinkedList<>();

        for (Trip trip: userTrips) {
            long placeId = trip.getStartPlaceId();
            ar.edu.itba.paw.model.Place place = ps.findById(placeId).get();
            dataPairList.add(new DataPair<>(trip, place));

        }
        mav.addObject("userTripsList",dataPairList);
        mav.addObject("dateFormat",dateFormat);
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
            return mav;
        }
        Place place = places.get(0);
        LOGGER.debug("Google Place name is {}", place.getName());

        Optional<ar.edu.itba.paw.model.Place> maybePlace = ps.findByGoogleId(place.getPlaceId());

        modelPlace = maybePlace.orElseGet(() -> ps.create(place.getPlaceId(), place.getName(), place.getLatitude(),
                place.getLongitude(), place.getAddress()));

        Trip trip = ts.create(modelPlace.getId(), form.getName(), form.getDescription(),
                DateManipulation.stringToCalendar(form.getStartDate()),
                DateManipulation.stringToCalendar(form.getEndDate()));
        String startDate = dateFormat.format(trip.getStartDate().getTime());
        TripUser tripUser = tus.create(trip.getId(), user.getId(), UserRole.ADMIN);
        TripPlace tripPlace = tps.create(trip.getId(), modelPlace.getId());
        //userTrips
        mav.setViewName("redirect:/home/trips");
        return mav;
    }

    @RequestMapping("/home/trip/{tripId}")
    public ModelAndView trip(@ModelAttribute("user") User user, @PathVariable(value = "tripId") long tripId) {
        ModelAndView mav = new ModelAndView("trip");
        Optional<Trip> maybeTrip = ts.findById(tripId);
        Trip trip = maybeTrip.get();
        boolean isTravelling = ts.isTravelling(user.getId(), tripId);
        boolean isAdmin = ts.userIsAdmin(user.getId(), tripId);
        String startDate = dateFormat.format(trip.getStartDate().getTime());
        String endDate = dateFormat.format(trip.getEndDate().getTime());
        List<ar.edu.itba.paw.model.Place> tripPlaces = ps.getTripPlaces(trip.getId());
        List<DataPair<User, UserRole>> tripUsersAndRoles = us.getTripUsersAndRoles(tripId);
        List<DataPair<Activity, ar.edu.itba.paw.model.Place>> tripActAndPlace = as.getTripActivitiesDetails(tripId);
        mav.addObject("isTravelling", isTravelling);
        mav.addObject("isAdmin",isAdmin);
        mav.addObject("places", tripPlaces);
        mav.addObject("usersAndRoles", tripUsersAndRoles);
        mav.addObject("actAndPlaces", tripActAndPlace);
        mav.addObject("trip", trip);
        mav.addObject("startDate", startDate);
        mav.addObject("endDate", endDate);
        return mav;
    }
    @RequestMapping("/home/trip/{tripId}/join")
    public ModelAndView joinTrip(@ModelAttribute("user") User user, @PathVariable(value = "tripId") long tripId) {
        ModelAndView mav = new ModelAndView("trip");
        TripUser tripUserOpt = tus.create(tripId, user.getId(), UserRole.MEMBER);
        String redirect = String.format("redirect:/home/trip/%d", tripId);
        mav.setViewName(redirect);
        return mav;
    }

}