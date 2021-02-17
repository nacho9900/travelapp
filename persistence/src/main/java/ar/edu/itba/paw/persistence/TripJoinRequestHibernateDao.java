package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripJoinRequestDao;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripJoinRequest;
import ar.edu.itba.paw.model.TripJoinRequestStatus;
import ar.edu.itba.paw.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class TripJoinRequestHibernateDao implements TripJoinRequestDao
{
    @PersistenceContext
    EntityManager em;

    @Override
    public Optional<TripJoinRequest> findById( long id ) {
        return Optional.ofNullable( em.find( TripJoinRequest.class, id ) );
    }

    @Override
    public List<TripJoinRequest> getAllByTripId( long tripId ) {
        TypedQuery<TripJoinRequest> query = em.createQuery(
                "select r from Trip as t inner join t.joinRequests as r where t.id = :tripId", TripJoinRequest.class );
        em.setProperty( "tripId", tripId );
        return query.getResultList();
    }

    @Override
    public List<TripJoinRequest> getAllByTripIdAndStatus( long tripId, TripJoinRequestStatus status ) {
        TypedQuery<TripJoinRequest> query = em.createQuery(
                "select r from TripJoinRequest as r inner join fetch r.user as u inner join r.trip as t where t.id = " +
                ":tripId and r.status = :status",
                TripJoinRequest.class );
        query.setParameter( "tripId", tripId );
        query.setParameter( "status", status );
        return query.getResultList();
    }

    @Override
    public Optional<TripJoinRequest> getLastByTripIdAndUsername( long tripId, String username ) {
        TypedQuery<TripJoinRequest> query = em.createQuery(
                "select r from Trip as t inner join t.joinRequests as r inner join r.user as u where t.id = :tripId " +
                "and u.email = :username " +
                "and r.createdOn >= ALL(select r2.createdOn from TripJoinRequest as r2 where r.trip = r2.trip " +
                "and r.user = r2.user)", TripJoinRequest.class );
        query.setParameter( "tripId", tripId );
        query.setParameter( "username", username );
        return query.getResultList().stream().findFirst();
    }

    @Override
    public TripJoinRequest update( TripJoinRequest tripJoinRequest ) {
        return em.merge( tripJoinRequest );
    }

    @Override
    public TripJoinRequest create( User user, Trip trip, String message ) {
        TripJoinRequest tripJoinRequest = new TripJoinRequest( user, trip, message, LocalDateTime.now(),
                                                               TripJoinRequestStatus.PENDING );
        em.persist( tripJoinRequest );
        return tripJoinRequest;
    }
}
