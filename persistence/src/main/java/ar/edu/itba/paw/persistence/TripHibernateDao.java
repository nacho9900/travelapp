package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripDao;
import ar.edu.itba.paw.model.PaginatedResult;
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
public class TripHibernateDao implements TripDao
{
    private static final int MAX_ROWS = 100;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Trip create( String name, String description, LocalDate startDate, LocalDate endDate ) {
        Trip trip = new Trip( name, description, startDate, endDate );
        em.persist( trip );
        return trip;
    }

    @Override
    public Optional<Trip> findById( long id ) {
        return Optional.ofNullable( em.find( Trip.class, id ) );
    }

    @Override
    public PaginatedResult<Trip> findUserTrips( long userId, int page, int perPage ) {
        String queryString = "from Trip as t left join t.members as m left join m.user as u where u.id = :userId ";

        final TypedQuery<Long> idsQuery = em.createQuery( "select t.id " + queryString + "order by t.startDate",
                                                          Long.class );
        final TypedQuery<Trip> tripsQuery = em.createQuery(
                "select t from Trip as t where t.id in (:ids) order by t.startDate", Trip.class );
        final Query queryCount = em.createQuery( "select count(t) " + queryString );
        idsQuery.setParameter( "userId", userId );
        queryCount.setParameter( "userId", userId );

        final int itemsPerPage = Math.min( perPage, MAX_ROWS );

        final int firstResults = ( page - 1 ) * itemsPerPage;
        idsQuery.setFirstResult( firstResults );
        idsQuery.setMaxResults( itemsPerPage );

        List<Long> ids = idsQuery.getResultList();

        if ( ids.isEmpty() ) {
            return PaginatedResult.getEmpty();
        }

        tripsQuery.setParameter( "ids", ids );
        Long total = (Long) queryCount.getSingleResult();
        List<Trip> trips = tripsQuery.getResultList();

        return new PaginatedResult<>( trips, total, itemsPerPage, page );
    }

    @Override
    public void deleteTrip( long tripId ) {
        Query tripDelete = em.createQuery( "delete Trip as t where t.id = :id" );
        tripDelete.setParameter( "id", tripId );
        tripDelete.executeUpdate();
    }

    @Override
    public Trip update( Trip trip ) {
        return em.merge( trip );
    }

    @Override
    public PaginatedResult<Trip> search( Double latitude, Double longitude, LocalDate from, LocalDate to, int page,
                                         int perPage ) {
        StringBuilder queryString = new StringBuilder(
                "from Trip as t inner join t.activities as a inner join a.place as p " + "where 1=1 " );

        if ( latitude != null && longitude != null ) {
            queryString.append( "and (2 * atan2(sqrt((power(sin(abs((p.latitude - :latitude) * pi()/180)/2),2)" +
                                "+ cos(p.latitude * pi()/180) * cos(:latitude * pi()/180)" +
                                "* power(sin((abs(p.longitude - :longitude) * pi()/180)/2) ,2))), " +
                                "sqrt(1 - ((power(sin(abs((p.latitude - :latitude) * pi()/180)/2),2)" +
                                "+ cos(p.latitude * pi()/180) * cos(:latitude * pi()/180)" +
                                "* power(sin((abs(p.longitude - :longitude) * pi()/180)/2) , 2))))) * 6371) < 10 " );
        }

        if ( from != null ) {
            queryString.append( "and t.startDate >= :from " );
        }

        if ( to != null ) {
            queryString.append( "and t.endDate <= :to " );
        }

        TypedQuery<Long> idsQuery = em.createQuery( "select t.id " + queryString + "order by t.startDate", Long.class );

        TypedQuery<Trip> tripsQuery = em.createQuery(
                "select distinct t from Trip as t  where t.id in (:ids) order by t.startDate", Trip.class );

        Query countQuery = em.createQuery( "select count(distinct t) " + queryString );

        if ( latitude != null && longitude != null ) {
            idsQuery.setParameter( "latitude", latitude );
            idsQuery.setParameter( "longitude", longitude );
            countQuery.setParameter( "latitude", latitude );
            countQuery.setParameter( "longitude", longitude );
        }

        if ( from != null ) {
            idsQuery.setParameter( "from", from );
            countQuery.setParameter( "from", from );
        }

        if ( to != null ) {
            idsQuery.setParameter( "to", to );
            countQuery.setParameter( "to", to );
        }

        final int itemsPerPage = Math.min( perPage, MAX_ROWS );

        final int firstResults = ( page - 1 ) * itemsPerPage;
        idsQuery.setFirstResult( firstResults );
        idsQuery.setMaxResults( itemsPerPage );

        List<Long> ids = idsQuery.getResultList();

        if ( ids.isEmpty() ) {
            return PaginatedResult.getEmpty();
        }

        tripsQuery.setParameter( "ids", ids );
        List<Trip> trips = tripsQuery.getResultList();
        final long total = (Long) countQuery.getSingleResult();
        return new PaginatedResult<>( trips, total, itemsPerPage, page );
    }
}
