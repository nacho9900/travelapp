package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.TripDao;
import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class TripServiceImpl implements TripService {

    @Autowired
    private TripDao td;

    @Override
    public Trip create(long startPlaceId, String name, String description, Calendar startDate, Calendar endDate) {
        return td.create(startPlaceId, name, description, startDate, endDate);
    }

    @Override
    public List<Trip> findUserTrips(long userId, int pageNum) {
        return td.findUserTrips(userId, pageNum);
    }

    @Override
    public int countUserTrips(long userId) {
        return td.countUserTrips(userId);
    }

    @Override
    public Optional<Trip> findById(long id) {
        return td.findById(id);
    }

    @Override
    public boolean userIsAdmin(long userId, long tripId) {
        return td.userIsAdmin(userId, tripId);
    }

    @Override
    public boolean isTravelling(long userId, long tripId) {
        return td.isTravelling(userId, tripId);
    }

    @Override
    public List<Trip> getAllTrips() {
        return td.getAllTrips();
    }

    @Override
    public List<Trip> findByName(String name) {
        return td.findByName(name);
    }
}
