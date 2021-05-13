package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Place;

import java.util.Optional;

public interface PlaceDao
{
    Place create( String googleId, String name, double latitude, double longitude, String address );

    Optional<Place> findById( long id );

    Optional<Place> findByGoogleId( String googleId );
}
