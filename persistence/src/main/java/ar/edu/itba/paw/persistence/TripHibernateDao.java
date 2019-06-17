package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripDao;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripComment;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TripHibernateDao implements TripDao {

    private static final int MAX_ROWS = 6;

    @PersistenceContext
    EntityManager em;

    @Override
    public Trip create(long userId, long startPlaceId, String name, String description, LocalDate startDate, LocalDate endDate) {
        Trip trip = new Trip(userId, startPlaceId, name, description, startDate, endDate);
        em.persist(trip);
        return trip;
    }

    @Override
    public Optional<Trip> findById(long id) {
        return Optional.of(em.find(Trip.class, id));
    }

    @Override
    public List<Trip> findByName(String name) {
        final TypedQuery<Trip> query = em.createQuery("From Trip as t where t.name like :name", Trip.class);
        query.setParameter("name", "%" + name + "%");
        query.setMaxResults(MAX_ROWS);
        return query.getResultList();
    }

    @Override
    public List<Trip> getAllTrips(int pageNum) {
        final TypedQuery<Trip> query = em.createQuery("From Trip", Trip.class);
        query.setFirstResult((pageNum - 1) * MAX_ROWS);
        query.setMaxResults(MAX_ROWS);
        return query.getResultList();
    }

    @Override
    public List<Trip> findUserCreatedTrips(long userId) {
        final TypedQuery<Trip> query = em.createQuery("From Trip as t where t.adminId = :userId ", Trip.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public void deleteTrip(long tripId) {
        Query tripDelete = em.createQuery("delete Trip as t where t.id = :id");
        tripDelete.setParameter("id", tripId);
        tripDelete.executeUpdate();
    }

    public int countAllTrips() {
        TypedQuery<Long> query = em.createQuery("select count(*) from Trip", Long.class);
        return query.getSingleResult().intValue();
    }

    @Override
    public List<Trip> findByCategory(String category) {
        final TypedQuery<Trip> query = em.createQuery("select t From Trip as t, Activity as a" +
                " where a.trip.id = t.id and a.category like :category", Trip.class);
        query.setParameter("category", category);
        query.setMaxResults(MAX_ROWS);
        return query.getResultList();
    }

    @Override
    public List<Trip> findByPlace(String placeName) {
        final TypedQuery<Trip> query = em.createQuery("select t From Trip as t, Place as p" +
                " where t.startPlaceId = p.id and lower(p.address) like lower(:placeName)", Trip.class);
        query.setParameter("placeName", "%" + placeName + "%");
        query.setMaxResults(MAX_ROWS);
        return query.getResultList();
    }

    @Override
    public List<Trip> findWithFilters(Map<String, Object> filterMap) {
        final TypedQuery<Trip> query = em.createQuery("select distinct t From Trip as t, Place as p, Activity as a where " +
                        filtersQuery(filterMap), Trip.class);
        setQueryParameters(query, filterMap);
        query.setMaxResults(MAX_ROWS);
        return query.getResultList();
    }

    private void setQueryParameters(TypedQuery<Trip> query, Map<String, Object> filterMap) {
        for(String filter : filterMap.keySet()) {
            if(filter.equals("placeName")) {
                query.setParameter(filter, "%" +  (String)filterMap.get(filter) + "%");
            }
            else {
                query.setParameter(filter, filterMap.get(filter));
            }
        }
    }

    private String filtersQuery(Map<String, Object> filterMap) {
        int count = 0;
        StringBuilder buffer = new StringBuilder();
        for(String filter : filterMap.keySet()) {
            switch(filter) {
                case "category":
                    if(count != 0) buffer.append(" and ");
                    buffer.append("a.trip.id = t.id and a.category like :category");
                    count++;
                    break;
                case "placeName":
                    if(count != 0) buffer.append(" and ");
                    buffer.append("t.startPlaceId = p.id and lower(p.address) like lower(:placeName) ");
                    count++;
                    break;
                case "startDate":
                    if(count != 0) buffer.append(" and ");
                    buffer.append("t.startDate = :startDate");
                    count++;
                    break;
                case "endDate":
                    if(count != 0) buffer.append(" and ");
                    buffer.append("t.endDate = :endDate");
                    count++;
                    break;
            }
        }
        return buffer.toString();
    }

    @Override
    public List<TripComment> getTripComments(long tripId) {
        final TypedQuery<TripComment> query = em.createQuery("From TripComment as tc where tc.trip.id = :tripId", TripComment.class);
        query.setParameter("tripId", tripId);
        return query.getResultList();
    }
}
