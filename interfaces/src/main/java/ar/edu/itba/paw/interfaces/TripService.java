package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public interface TripService {

    public Trip create(User createdBy, Place place, String name, String description, Calendar startDate, Calendar endDate);
    public Optional<Trip> findById(long id);
    public List<Trip> getAllTrips();
    public List<Trip> findByName(String name);
}
