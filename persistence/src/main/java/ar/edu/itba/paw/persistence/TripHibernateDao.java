package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripDao;
import ar.edu.itba.paw.model.PaginatedResult;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.TripRate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TripHibernateDao implements TripDao
{
    private static final int MAX_ROWS = 12;

    @PersistenceContext
    EntityManager em;

    @Override
    public Trip create( Trip trip ) {
        em.persist( trip );
        return trip;
    }

    @Override
    public Trip create( long userId, String name, String description, LocalDate startDate, LocalDate endDate ) {
        Trip trip = new Trip( userId, name, description, startDate, endDate );
        em.persist( trip );
        return trip;
    }

    @Override
    public Optional<Trip> findById( long id ) {
        return Optional.ofNullable( em.find( Trip.class, id ) );
    }

    @Override
    public List<Trip> findByName( String name ) {
        final TypedQuery<Trip> query = em.createQuery( "From Trip as t where lower(t.name) like lower(:name)",
                                                       Trip.class );
        query.setParameter( "name", "%" + name + "%" );
        query.setMaxResults( MAX_ROWS );
        return query.getResultList();
    }

    @Override
    public List<Trip> getAllTrips( int pageNum ) {
        final TypedQuery<Trip> query = em.createQuery( "From Trip", Trip.class );
        query.setFirstResult( ( pageNum - 1 ) * MAX_ROWS );
        query.setMaxResults( MAX_ROWS );
        return query.getResultList();
    }

    @Override
    public PaginatedResult<Trip> findUserTrips( long userId, int page ) {
        String queryString = "from Trip as t left join t.members as m left join m.user as u where u.id = :userId ";

        final TypedQuery<Trip> query = em.createQuery( "select t " + queryString + "order by t.startDate", Trip.class );
        final Query queryCount = em.createQuery( "select count(t) " + queryString );
        query.setParameter( "userId", userId );
        queryCount.setParameter( "userId", userId );
        int firstResults = ( page - 1 ) * MAX_ROWS;
        query.setFirstResult( firstResults );
        query.setMaxResults( MAX_ROWS );
        List<Trip> result = query.getResultList();
        Long total = (Long) queryCount.getSingleResult();

        return new PaginatedResult<>( result, total, MAX_ROWS, total != 0 && firstResults >= total );
    }

    @Override
    public void deleteTrip( long tripId ) {
        Query tripDelete = em.createQuery( "delete Trip as t where t.id = :id" );
        tripDelete.setParameter( "id", tripId );
        tripDelete.executeUpdate();
    }

    @Override
    public List<Trip> findByCategory( String category ) {
        final TypedQuery<Trip> query = em.createQuery(
                "select t From Trip as t, Activity as a" + " where a.trip.id = t.id and a.category like :category",
                Trip.class );
        query.setParameter( "category", category );
        query.setMaxResults( MAX_ROWS );
        return query.getResultList();
    }

    @Override
    public List<Trip> findByPlace( String placeName ) {
        final TypedQuery<Trip> query = em.createQuery(
                "select t From Trip as t, Place as p" + " where t.startPlaceId = p.id and lower(p.address) like " +
                "lower" + "(:placeName)", Trip.class );
        query.setParameter( "placeName", "%" + placeName + "%" );
        query.setMaxResults( MAX_ROWS );
        return query.getResultList();
    }

    @Override
    public List<TripComment> getTripComments( long tripId ) {
        final TypedQuery<TripComment> query = em.createQuery( "From TripComment as tc where tc.trip.id = :tripId",
                                                              TripComment.class );
        query.setParameter( "tripId", tripId );
        return query.getResultList();
    }

    @Override
    public List<TripRate> getTripRates( long tripId ) {
        final TypedQuery<TripRate> query = em.createQuery( "From TripRate as tr where tr.trip.id = :tripId",
                                                           TripRate.class );
        query.setParameter( "tripId", tripId );
        return query.getResultList();
    }

    @Override
    public boolean isUserMember( long tripId, String username ) {
        final TypedQuery<Trip> query = em.createQuery( "select trip from Trip as trip left join trip.members as m " +
                                                       "left join m.user as user where trip.id = :tripId and user" +
                                                       ".email = :username", Trip.class );

        query.setParameter( "tripId", tripId );
        query.setParameter( "username", username );

        return query.getResultList().size() > 0;
    }

    @Override
    public boolean isUserOwnerOrAdmin( long tripId, String username ) {
        final TypedQuery<Trip> query = em.createQuery(
                "select trip " + "from Trip as trip left join trip.members as m left join m.user as user " +
                "where trip.id = :tripId and user.email like :username and m.role in ('ADMIN', 'OWNER')", Trip.class );

        query.setParameter( "tripId", tripId );
        query.setParameter( "username", username );

        return query.getResultList().size() > 0;
    }

    @Override
    public Trip update( Trip trip ) {
        return em.merge( trip );
    }

    @Override
    public PaginatedResult<Trip> search( String text, Double latitude, Double longitude, LocalDate from, LocalDate to
            , int page ) {
        StringBuilder queryString = new StringBuilder(
                "from Trip as t inner join t.activities as a inner join a.place as p " + "where 1=1 " );

        if ( latitude != null && longitude != null ) {
            queryString.append( "and (2 * atan2(sqrt((power(sin(abs((p.latitude - :latitude) * pi()/180)/2),2)" +
                                "+ cos(p.latitude * pi()/180) * cos(:latitude * pi()/180)" +
                                "* power(sin((abs(p.longitude - :longitude) * pi()/180)/2) ,2))), " +
                                "sqrt(1 - ((power(sin(abs((p.latitude - :latitude) * pi()/180)/2),2)" +
                                "+ cos(p.latitude * pi()/180) * cos(:latitude * pi()/180)" +
                                "* power(sin((abs(p.longitude - :longitude) * pi()/180)/2) , 2))))) * 6371) < 10" );
        }

        if ( from != null ) {
            queryString.append( "and t.startDate >= :from " );
        }

        if ( to != null ) {
            queryString.append( "and t.endDate <= :to " );
        }

        TypedQuery<Trip> query = em.createQuery( "select distinct t " + queryString.toString() + "order by t.startDate",
                                                 Trip.class );
        Query queryCount = em.createQuery( "select count( distinct t) " + queryString.toString() );

        if ( latitude != null && longitude != null ) {
            query.setParameter( "latitude", latitude );
            query.setParameter( "longitude", longitude );
            queryCount.setParameter( "latitude", latitude );
            queryCount.setParameter( "longitude", longitude );
        }

        if ( from != null ) {
            query.setParameter( "from", from );
            queryCount.setParameter( "from", from );
        }

        if ( to != null ) {
            query.setParameter( "to", to );
            queryCount.setParameter( "to", to );
        }

        int firstResults = ( page - 1 ) * MAX_ROWS;
        query.setFirstResult( firstResults );
        query.setMaxResults( MAX_ROWS );

        List<Trip> results = query.getResultList();
        Long total = (Long) queryCount.getSingleResult();
        return new PaginatedResult<>( results, total, MAX_ROWS, total != 0 && firstResults >= total );
    }
}
