package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripPicture;

import java.util.Optional;

public interface TripPicturesService
{
    TripPicture create( Trip trip, String name, byte[] image );

    TripPicture create( Trip trip, String name, String imageBase64 );

    TripPicture update( TripPicture tripPicture, Trip trip, String name, String imageBase64 );

    TripPicture update( TripPicture tripPicture, Trip trip, String name, byte[] image );

    Optional<TripPicture> findByTripId( long tripId );

    byte[] resize( byte[] image, int width, int height );

    byte[] resizeHeight( byte[] image, int height );

    byte[] resizeWidth( byte[] image, int width );
}
