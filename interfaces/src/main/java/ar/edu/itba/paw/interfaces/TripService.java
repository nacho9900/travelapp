package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.PaginatedResult;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;

import java.time.LocalDate;
import java.util.Optional;

public interface TripService
{
    Trip create( User owner, String name, String description, LocalDate startDate, LocalDate endDate );

    Optional<Trip> findById( long id );

    PaginatedResult<Trip> getAllUserTrips( User user, int page, int perPage );

    boolean isUserOwnerOrAdmin( long tripId, String username );

    boolean isUserMember( long tripId, String username );

    Trip update( Trip trip, String name, String description, LocalDate startDate, LocalDate endDate );

    PaginatedResult<Trip> search( String text, Double latitude, Double longitude, LocalDate from, LocalDate to, int page, int perPage );
}
