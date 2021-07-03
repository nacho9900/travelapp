package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.PaginatedResult;
import ar.edu.itba.paw.model.Trip;

import java.time.LocalDate;
import java.util.Optional;

public interface TripDao
{
    Trip create( String name, String description, LocalDate startDate, LocalDate endDate );

    Optional<Trip> findById( long id );

    PaginatedResult<Trip> findUserTrips( long userId, int page, int perPage );

    void deleteTrip( long tripId );

    Trip update( Trip trip );

    PaginatedResult<Trip> search( Double latitude, Double longitude, LocalDate from, LocalDate to, int page, int perPage );
}
