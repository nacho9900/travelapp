package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.TripDao;
import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TripServiceImpl implements TripService {

    @Autowired
    private TripDao td;

    @Override
    public Trip create(User user, Place place, String name, String description, Calendar startDate, Calendar endDate) {
        return td.create(user, place, name, description, startDate, endDate);
    }

    @Override
    public Optional<Trip> findById(long id) {
        return td.findById(id);
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
