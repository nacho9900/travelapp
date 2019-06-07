package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripDao;
import ar.edu.itba.paw.model.Trip;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public class TripHibernateDao implements TripDao {

    @PersistenceContext
    EntityManager em;


    @Override
    public Trip create(long startPlaceId, String name, String description, Calendar startDate, Calendar endDate) {

        return null;
    }

    @Override
    public Optional<Trip> findById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Trip> findUserTrips(long userId, int pageNum) {
        return null;
    }

    @Override
    public boolean userIsAdmin(long userId, long tripId) {
        return false;
    }

    @Override
    public boolean isTravelling(long userId, long tripId) {
        return false;
    }

    @Override
    public List<Trip> getAllTrips() {
        return null;
    }

    @Override
    public int countUserTrips(long userId) {
        return 0;
    }

    @Override
    public List<Trip> findByName(String name) {
        return null;
    }
}
