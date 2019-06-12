package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripCommentsDao;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.User;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public class TripCommentHibernateDao implements TripCommentsDao {

    @PersistenceContext
    EntityManager em;

    @Override
    public TripComment create(User user, Trip trip, String comment) {
        TripComment tc = new TripComment(trip, comment, user, LocalDate.now());
        em.persist(tc);
        return tc;
    }

    @Override
    public Optional<TripComment> findById(long id) {
        return Optional.of(em.find(TripComment.class, id));
    }

    @Override
    public void delete(TripComment tripComment) {
        em.remove(tripComment);
    }
}
