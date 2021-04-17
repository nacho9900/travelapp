package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.ActivityDao;
import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Trip;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ActivityHibernateDao implements ActivityDao
{
    private static final int MAX_ROWS = 5;

    @PersistenceContext
    EntityManager em;

    @Override
    public Optional<Activity> findById( long id ) {
        return Optional.ofNullable( em.find( Activity.class, id ) );
    }

    @Override
    public Activity create( String name, Trip trip, LocalDate startDate, LocalDate endDate, Place place ) {
        Activity activity = new Activity( name, place, trip, startDate, endDate );
        activity.setPlace( place );
        em.persist( activity );
        return activity;
    }

    @Override
    public void deleteActivities( long tripId ) {
        Query activityDelete = em.createQuery( "delete Activity as a where a.trip.id = :tripId" );
        activityDelete.setParameter( "tripId", tripId );
        activityDelete.executeUpdate();
    }

    @Override
    public void deleteActivity( long activityId ) {
        Query activityDelete = em.createQuery( "delete Activity as a where a.id = :activityId" );
        activityDelete.setParameter( "activityId", activityId );
        activityDelete.executeUpdate();
    }

    @Override
    public List<Activity> getTripActivities( long tripId ) {
        final TypedQuery<Activity> query = em.createQuery(
                "From Activity as a where a.trip.id = :tripId order by a.startDate, a.id", Activity.class );
        query.setParameter( "tripId", tripId );
        return query.getResultList();
    }

    @Override
    public boolean isActivityPartOfTheTrip( long tripId, long activityId ) {
        final TypedQuery<Activity> query = em.createQuery(
                "select a from Trip as t inner join t.activities as a where t.id = :tripId and a.id = :activityId",
                Activity.class );

        query.setParameter( "tripId", tripId );
        query.setParameter( "activityId", activityId );

        return query.getResultList().size() > 0;
    }

    @Override
    public Activity update( Activity activity ) {
        return em.merge( activity );
    }

}
