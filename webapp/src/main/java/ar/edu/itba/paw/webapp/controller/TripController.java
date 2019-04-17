package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.TripDao;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class TripController extends MainController{

    @Autowired
    TripDao td;

    @RequestMapping("/home/create-trip")
    public ModelAndView createTripGet() {
        return new ModelAndView("createTrip");
    }

    @RequestMapping("/home/trips")
    public ModelAndView getUserTrips(@ModelAttribute("user") User user) {
        ModelAndView mav = new ModelAndView("userTrips");
        List<Trip> userTrips = td.findUserTrips(user.getId());
        mav.addObject("userTrips",userTrips);
        return mav;
    }
}