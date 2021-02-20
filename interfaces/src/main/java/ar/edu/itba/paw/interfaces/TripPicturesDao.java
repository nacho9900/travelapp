package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripPicture;

import java.util.Optional;

public interface TripPicturesDao
{
    TripPicture create( Trip trip, String name, byte[] image );

    Optional<TripPicture> findByTripId( long tripId );

    TripPicture update( TripPicture tripPicture );
}
