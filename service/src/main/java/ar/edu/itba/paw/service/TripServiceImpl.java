package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.TripDao;
import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class TripServiceImpl implements TripService {

    @Autowired
    private TripDao td;

    @Override
    public Trip create(long userId, long startPlaceId, String name, String description, Calendar startDate, Calendar endDate) {
        return td.create(userId, startPlaceId, name, description, startDate, endDate);
    }

    @Override
    public Optional<Trip> findById(long id) {
        return td.findById(id);
    }

    @Override
    public List<Trip> findByName(String name) {
        return td.findByName(name);
    }

    @Override
    public List<Trip> getAllTrips() {
        return td.getAllTrips();
    }

    public Set<Trip> getAllUserTrips(User user, int pageNum) {
        System.out.println("IN GET ALL USER TRIPS");
        System.out.println("USER TRIPS: " + user.getTrips());
        Set<Trip> trips = new HashSet<>(user.getTrips());


        List<Trip> createdTrips = td.findUserCreatedTrips(user.getId(), pageNum);
        trips.addAll(createdTrips);
        return trips;
    }

    @Override
    public List<Place> findTripPlaces(Trip trip) {
        List<Activity> activities = trip.getActivities();
        List<Place> places = new LinkedList<>();
        for(Activity activity: activities) {
            places.add(activity.getPlace());
        }
        return places;
    }


}
