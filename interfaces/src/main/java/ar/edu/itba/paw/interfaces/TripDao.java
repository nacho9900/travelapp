package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.Trip;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public interface TripDao {

    public Trip create(long userId, long startPlaceId, String name, String description, Calendar startDate, Calendar endDate);
    public Optional<Trip> findById(long id);
    public List<Trip> findByName(String name);
    public List<Trip> getAllTrips();

    public List<Trip> findUserTrips(long userId, int pageNum);
    public List<Trip> findUserCreatedTrips(long userId, int pageNum);

}
