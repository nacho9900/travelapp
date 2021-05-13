package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripCommentsDao;
import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.TripMember;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Repository
public class TripCommentHibernateDao implements TripCommentsDao
{
    @PersistenceContext
    EntityManager em;

    @Override
    public TripComment create( TripMember member, String comment ) {
        TripComment tc = new TripComment( member, comment, LocalDateTime.now( ZoneOffset.UTC) );
        em.persist( tc );
        return tc;
    }

    @Override
    public Optional<TripComment> findById( long id ) {
        return Optional.ofNullable( em.find( TripComment.class, id ) );
    }

    @Override
    public void deleteAllByMemberId( long memberId ) {
        Query commentDelete = em.createQuery( "delete TripComment as tc where tc.member.id = :memberId" );
        commentDelete.setParameter( "memberId", memberId );
        commentDelete.executeUpdate();
    }

    @Override
    public List<TripComment> getAllByTripId( long tripId ) {
        TypedQuery<TripComment> query = em.createQuery(
                "select c from TripComment as c inner join fetch c.member as m inner join m.trip as t where t.id = " +
                ":tripId order by c.createdOn desc", TripComment.class );
        query.setParameter( "tripId", tripId );
        return query.getResultList();
    }
}
