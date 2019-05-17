package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Trip;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public interface TripDao {

    public Trip create(long startPlaceId, String name, String description, Calendar startDate, Calendar endDate);
    public Optional<Trip> findById(long id);
    public List<Trip> findUserTrips(long userId);
    public boolean userIsAdmin(long userId, long tripId);
    public boolean isTravelling(long userId, long tripId);
    public List<Trip> getAllTrips();

}
