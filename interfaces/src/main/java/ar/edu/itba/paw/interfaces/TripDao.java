package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.PaginatedResult;
import ar.edu.itba.paw.model.Trip;

import java.time.LocalDate;
import java.util.Optional;

public interface TripDao
{
    Trip create( Trip trip );

    Trip create( String name, String description, LocalDate startDate, LocalDate endDate );

    Optional<Trip> findById( long id );

    PaginatedResult<Trip> findUserTrips( long userId, int page, int perPage );

    void deleteTrip( long tripId );

    boolean isUserMember( long tripId, String username );

    boolean isUserOwnerOrAdmin( long tripId, String username );

    Trip update( Trip trip );

    PaginatedResult<Trip> search( String text, Double latitude, Double longitude, LocalDate from, LocalDate to, int page, int perPage );
}
