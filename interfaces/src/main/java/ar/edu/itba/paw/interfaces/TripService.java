package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.PaginatedResult;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exception.EntityNotFoundException;
import ar.edu.itba.paw.model.exception.InvalidDateRangeException;
import ar.edu.itba.paw.model.exception.InvalidUserException;
import ar.edu.itba.paw.model.exception.UserNotOwnerOrAdminException;

import java.time.LocalDate;
import java.util.Optional;

public interface TripService
{
    Trip create( String ownerUserName, String name, String description, LocalDate startDate, LocalDate endDate )
            throws InvalidUserException, InvalidDateRangeException;

    Optional<Trip> findById( long id );

    PaginatedResult<Trip> getAllUserTrips( User user, int page, int perPage );

    boolean isUserOwnerOrAdmin( long tripId, String username );

    boolean isUserMember( long tripId, String username );

    Trip update( long id, String name, String description, LocalDate startDate, LocalDate endDate,
                 String editorUsername )
            throws EntityNotFoundException, UserNotOwnerOrAdminException, InvalidDateRangeException;

    PaginatedResult<Trip> search( Double latitude, Double longitude, LocalDate from, LocalDate to, int page, int perPage );
}
