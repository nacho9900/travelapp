package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripMemberDao;
import ar.edu.itba.paw.model.TripMember;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class TripMemberHibernateDao implements TripMemberDao
{
    @PersistenceContext
    EntityManager em;

    @Override
    public Optional<TripMember> findById( long id ) {
        return Optional.ofNullable( em.find( TripMember.class, id ) );
    }

    @Override
    public List<TripMember> getAllByTripId( long tripId ) {
        TypedQuery<TripMember> query = em.createQuery(
                "select m from Trip as t inner join t.members as m where t.id " + "= :tripId ", TripMember.class );
        query.setParameter( "tripId", tripId );
        return query.getResultList();
    }

    @Override
    public boolean memberBelongsToTheTrip( long id, long tripId ) {
        TypedQuery<TripMember> query = em.createQuery( "select m from Trip as t inner join t.members as m where t.id " +
                                                       "= :tripId and m.id = :id ", TripMember.class );
        query.setParameter( "tripId", tripId );
        query.setParameter( "id", id );
        return query.getResultList().size() > 0;
    }

    @Override
    public Optional<TripMember> findByTripIdAndUsername( String username, long tripId ) {
        TypedQuery<TripMember> query = em.createQuery( "select m from TripMember as m inner join m.user as u inner " +
                                                       "join m.trip as t where t.id = :tripId and u.email = " +
                                                       ":username", TripMember.class );
        query.setParameter( "tripId", tripId );
        query.setParameter( "username", username );
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void delete( long id ) {
        TypedQuery<TripMember> query = em.createQuery( "delete TripMember as m where m.id = :id", TripMember.class );
        query.setParameter( "id", id );
        query.executeUpdate();
    }

    @Override
    public TripMember update(TripMember tripMember) {
        return em.merge( tripMember );
    }
}
