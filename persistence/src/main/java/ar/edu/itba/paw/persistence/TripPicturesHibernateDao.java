package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripPicturesDao;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripPicture;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class TripPicturesHibernateDao implements TripPicturesDao
{
    @PersistenceContext
    EntityManager em;

    @Override
    public TripPicture create( Trip trip, String name, byte[] image ) {
        TripPicture tp = new TripPicture( trip, name, image );
        em.persist( tp );
        return tp;
    }

    @Override
    public Optional<TripPicture> findByTripId( long tripId ) {
        final TypedQuery<TripPicture> query = em.createQuery( "From TripPicture as tp where tp.trip.id = :tripId",
                                                              TripPicture.class );
        query.setParameter( "tripId", tripId );
        return query.getResultList().stream().findFirst();
    }

    @Override
    public TripPicture update( TripPicture tripPicture ) {
        return em.merge( tripPicture );
    }
}
