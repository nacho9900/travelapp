package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class TripServiceImpl implements TripService {

    @Autowired
    private TripDao td;

    @Autowired
    private ActivityDao ad;

    @Autowired
    private TripCommentsDao tcd;

    @Autowired
    private TripPicturesDao tpd;

    @Autowired
    private UserDao ud;

    @Override
    public Trip create(long userId, long startPlaceId, String name, String description, LocalDate startDate, LocalDate endDate) {
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

    @Override
    public void addActivityToTrip(long actId, long tripId) {
        Optional<Trip> ot = td.findById(tripId);
        Optional<Activity> oa = ad.findById(actId);
        if(ot.isPresent() && oa.isPresent()) {
            ot.get().getActivities().add(oa.get());
        }
    }

    @Override
    public void addUserToTrip(long userId, long tripId) {
        Optional<User> ou = ud.findById(userId);
        Optional<Trip> ot = td.findById(tripId);
        if(ou.isPresent() && ot.isPresent()) {
            ou.get().getTrips().add(ot.get());
            ot.get().getUsers().add(ou.get());
        }
    }

    @Override
    public void removeUserFromTrip(long userId, long tripId) {
        Optional<User> ou = ud.findById(userId);
        Optional<Trip> ot = td.findById(tripId);
        if(ou.isPresent() && ot.isPresent()) {
            ou.get().getTrips().remove(ot.get());
            ot.get().getUsers().remove(ou.get());
        }
    }

    @Override
    public void deleteTrip(long tripId) {
        Optional<Trip> ot = td.findById(tripId);
        if(ot.isPresent()) {
            Trip trip = ot.get();
            for(TripComment comment: trip.getComments()) {
                tcd.delete(comment);
            }
            for(Activity activity: trip.getActivities()) {
                ad.delete(activity);
            }
            tpd.deleteByTripId(tripId);
        }
        td.deleteTrip(tripId);
    }

    @Override
    public void addCommentToTrip(long commentId, long tripId) {
        Optional<TripComment> otc = tcd.findById(commentId);
        Optional<Trip> ot = td.findById(tripId);
        if(otc.isPresent() && ot.isPresent()) {
            ot.get().getComments().add(otc.get());
        }
    }

}
