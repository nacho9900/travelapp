package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripRateDao;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.TripRate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Repository
public class TripRateHibernateDao implements TripRateDao
{
    @PersistenceContext
    private EntityManager em;

    @Override
    public TripRate create( TripMember member, int rate ) {
        TripRate tripRate = new TripRate( rate, LocalDateTime.now( ZoneOffset.UTC ), member );
        em.persist( tripRate );
        return tripRate;
    }

    @Override
    public Optional<TripRate> findById( long id ) {
        return Optional.ofNullable( em.find( TripRate.class, id ) );
    }

    @Override
    public void delete( long id ) {
        Query deleteQuery = em.createQuery( "delete TripRate as tripRate where tripRate.id = :id" );
        deleteQuery.setParameter( "id", id );
        deleteQuery.executeUpdate();
    }

    @Override
    public void update( TripRate tripRate ) {
        em.merge( tripRate );
    }

    @Override
    public Optional<TripRate> findByUserAndTrip( long tripId, long userId ) {
        final TypedQuery<TripRate> query = em.createQuery(
                "From TripRate as tr where tr.trip.id = :tripId and tr.user.id = :userId", TripRate.class );
        query.setParameter( "tripId", tripId );
        query.setParameter( "userId", userId );
        return query.getResultList().stream().findAny();
    }

    @Override
    public void deleteByMemberId( long memberId ) {
        Query query = em.createQuery( "delete TripRate as tr where tr.member.id = :memberId" );
        query.setParameter( "memberId", memberId );
        query.executeUpdate();
    }
}
