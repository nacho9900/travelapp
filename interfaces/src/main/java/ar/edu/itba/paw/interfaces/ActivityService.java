package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.exception.ActivityNotPartOfTheTripException;
import ar.edu.itba.paw.model.exception.EntityNotFoundException;
import ar.edu.itba.paw.model.exception.InvalidDateRangeException;
import ar.edu.itba.paw.model.exception.UserNotOwnerOrAdminException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ActivityService
{
    Optional<Activity> findById( long id );

    Activity create( long tripId, String name, LocalDate startDate, LocalDate endDate, Place place, String username )
            throws EntityNotFoundException, UserNotOwnerOrAdminException, InvalidDateRangeException;

    List<Activity> findByTrip( long tripId );

    boolean isActivityPartOfTheTrip( long tripId, long activityId );

    Activity update( long id, long tripId, String name, LocalDate startDate, LocalDate endDate, Place place,
                     String username )
            throws EntityNotFoundException, UserNotOwnerOrAdminException, InvalidDateRangeException, ActivityNotPartOfTheTripException;

    void delete( long id, long tripId, String username )
            throws EntityNotFoundException, ActivityNotPartOfTheTripException, UserNotOwnerOrAdminException;
}
