package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.*;

import java.time.LocalDate;
import java.util.*;

public interface TripService
{
    Trip create( User owner, String name, String description, LocalDate startDate, LocalDate endDate );

    Optional<Trip> findById( long id );

    PaginatedResult<Trip> getAllUserTrips( User user, int page );

    boolean isUserOwnerOrAdmin( long tripId, String username );

    boolean isUserMember( long tripId, String username );

    Trip update( Trip trip, String name, String description, LocalDate startDate, LocalDate endDate );

    PaginatedResult<Trip> search( String text, Double latitude, Double longitude, LocalDate from, LocalDate to, int page );
}
