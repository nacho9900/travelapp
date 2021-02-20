package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.*;

import java.time.LocalDate;
import java.util.*;

public interface TripService
{
    Trip create( long createdBy, long place, String name, String description, LocalDate startDate, LocalDate endDate );

    Trip create( Trip trip );

    Optional<Trip> findById( long id );

    List<Trip> getAllTrips( int pageNum );

    List<Trip> findByName( String name );

    PaginatedResult<Trip> getAllUserTrips( User user, int page );

    List<Trip> findByCategory( String category );

    List<Trip> findByPlace( String placeName );

    boolean isUserOwnerOrAdmin( long tripId, String username );

    boolean isUserMember( long tripId, String username );

    Trip update( Trip trip, String name, String description, LocalDate startDate, LocalDate endDate );

    PaginatedResult<Trip> search( String text, Double latitude, Double longitude, LocalDate from, LocalDate to, int page );
}
