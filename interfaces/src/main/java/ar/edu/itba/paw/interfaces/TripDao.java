package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.PaginatedResult;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.TripRate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TripDao
{
    Trip create( Trip trip );

    Trip create( long userId, String name, String description, LocalDate startDate, LocalDate endDate );

    Optional<Trip> findById( long id );

    List<Trip> findByName( String name );

    List<Trip> getAllTrips( int pageNum );

    PaginatedResult<Trip> findUserTrips( long userId, int page );

    void deleteTrip( long tripId );

    List<Trip> findByCategory( String category );

    List<Trip> findByPlace( String placeName );

    List<TripComment> getTripComments( long tripId );

    List<TripRate> getTripRates( long tripId );

    boolean isUserMember( long tripId, String username );

    boolean isUserOwnerOrAdmin( long tripId, String username );

    Trip update( Trip trip );

    PaginatedResult<Trip> search( String text, Double latitude, Double longitude, LocalDate from, LocalDate to, int page );
}
