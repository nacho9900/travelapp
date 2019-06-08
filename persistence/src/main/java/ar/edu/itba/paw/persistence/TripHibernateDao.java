package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripDao;
import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Repository
public class TripHibernateDao implements TripDao {

    private static final int MAX_ROWS = 5;

    @PersistenceContext
    EntityManager em;

    @Override
    public Trip create(long userId, long startPlaceId, String name, String description, Calendar startDate, Calendar endDate) {
        Trip trip = new Trip(userId, startPlaceId, name, description, startDate, endDate);
        em.persist(trip);
        return trip;
    }

    @Override
    public Optional<Trip> findById(long id) {
        return Optional.of(em.find(Trip.class, id));
    }

    @Override
    public List<Trip> findUserTrips(long userId, int pageNum) {
        final TypedQuery<Trip> query = em.createQuery("From Trip where User:id like :userId ", Trip.class);
        query.setParameter("userId", userId);
        query.setFirstResult((pageNum - 1) * MAX_ROWS);
        query.setMaxResults(MAX_ROWS);
        return query.getResultList();
    }


    @Override
    public List<Trip> findByName(String name) {
        final TypedQuery<Trip> query = em.createQuery("From Trip as t where t.name like :name", Trip.class);
        query.setParameter("name", name);
        query.setMaxResults(MAX_ROWS);
        return query.getResultList();
    }

    @Override
    public List<Trip> getAllTrips() {
        int pageNum = 1; //TODO
        final TypedQuery<Trip> query = em.createQuery("From Trip", Trip.class);
        query.setFirstResult((pageNum - 1) * MAX_ROWS);
        query.setMaxResults(MAX_ROWS);
        return query.getResultList();
    }


}
