package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.ActivityDao;
import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Trip;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class ActivityHibernateDao implements ActivityDao {

    private static final int MAX_ROWS = 5;

    @PersistenceContext
    EntityManager em;

    @Override
    public Optional<Activity> findById(long id) {
        return Optional.of(em.find(Activity.class, id));
    }

    @Override
    public Optional<Activity> findByName(String name) {
        final TypedQuery<Activity> query = em.createQuery("From Activity as a where a.name like :name", Activity.class);
        query.setParameter("name", name);
        query.setMaxResults(MAX_ROWS);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Activity create(String name, String category, Place place, Trip trip) {
        Activity activity = new Activity(name, category, place, trip);
        em.persist(activity);
        return activity;
    }

    /*@Override
    public List<Activity> getTripActivities(long tripId) {
        return jdbcTemplate.query("SELECT * FROM activities, trip_activities WHERE trip_id = ? AND activities.id = activity_id ", ROW_MAPPER, tripId);
    }*/

    @Override
    public Optional<Place> getActivityPlace(long id) {
        final TypedQuery<Place> query = em.createQuery("select place From Activity as a where a.id = :id", Place.class);
        query.setParameter("id", id);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<Activity> findByCategory(String category) {
        final TypedQuery<Activity> query = em.createQuery("From Activity as a where a.category like :category", Activity.class);
        query.setParameter("category", category);
        return query.getResultList().stream().findFirst();
    }

}
