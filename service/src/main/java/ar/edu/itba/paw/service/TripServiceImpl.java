package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.model.Trip;

import java.util.Calendar;
import java.util.Optional;

public class TripServiceImpl implements TripService {

    @Override
    public Trip create(long placeid, String name, String description, Calendar startDate, Calendar endDate) {
        return null;
    }

    @Override
    public Optional<Trip> findById(long id) {
        return Optional.empty();
    }
}
