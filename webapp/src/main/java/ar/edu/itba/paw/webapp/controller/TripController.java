package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.PlaceService;
import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.model.DateManipulation;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.form.TripCreateForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;
import se.walkercrou.places.exception.GooglePlacesException;

import javax.validation.Valid;
import java.util.List;

@Controller
public class TripController extends MainController{

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    GooglePlaces client = new GooglePlaces("AIzaSyDf5BlyQV8TN06oWY_U7Z_MnqWjIci2k2M");

    @Autowired
    TripService ts;

    @Autowired
    PlaceService ps;

    @RequestMapping("/home/create-trip")
    public ModelAndView createTripGet(@ModelAttribute("createTripForm") final TripCreateForm form) {
        return new ModelAndView("createTrip");
    }

    @RequestMapping("/home/trips")
    public ModelAndView getUserTrips(@ModelAttribute("user") User user) {
        ModelAndView mav = new ModelAndView("userTrips");
        List<Trip> userTrips = ts.findUserTrips(user.getId());
        mav.addObject("userTrips",userTrips);
        return mav;
    }


    //TODO: checkear que no se creen nuevos places con google ids repetidos y crear trip-places user-roles etc
    @RequestMapping(value = "/home/create-trip", method = {RequestMethod.POST})
    public ModelAndView createTripPost(@ModelAttribute("user") User user,
                                       @Valid @ModelAttribute("createTripForm") final TripCreateForm form,
                                       final BindingResult errors) {

        ModelAndView mav = new ModelAndView("createTrip");
        if(errors.hasErrors()) return mav;
        LOGGER.debug("NO ERRORS IN CREATE TRIP FORM");
        List<Place> places;
        try {
            places = client.getPlacesByQuery(form.getPlaceInput(), GooglePlaces.MAXIMUM_RESULTS);
        }
        catch(GooglePlacesException gpe) {
            LOGGER.debug("INVALID GOOGLE PLACES QUERY LOCATION");
            return mav;
        }

        Place place = places.get(0);
        LOGGER.debug("Google Place name is {}", place.getName());
        ar.edu.itba.paw.model.Place modelPlace = ps.create(place.getPlaceId(), place.getName(), place.getLatitude(),
                place.getLongitude(), place.getAddress());

        Trip trip = ts.create(modelPlace.getId(), form.getName(), form.getDescription(), DateManipulation
                        .stringToCalendar(form.getStartDate()),
                DateManipulation.stringToCalendar(form.getEndDate()));

        mav.setViewName("userTrips");
        return mav;
    }
}