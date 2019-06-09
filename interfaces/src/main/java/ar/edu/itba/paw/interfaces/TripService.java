package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TripService {

    public Trip create(long createdBy, long place, String name, String description, Calendar startDate, Calendar endDate);
    public Optional<Trip> findById(long id);
    public List<Trip> getAllTrips();
    public List<Trip> findByName(String name);
    public Set<Trip> getAllUserTrips(User user, int pageNum);
    public List<Place> findTripPlaces(Trip trip);
}
