package ar.edu.itba.paw.service;


import ar.edu.itba.paw.interfaces.ActivityDao;
import ar.edu.itba.paw.interfaces.ActivityService;
import ar.edu.itba.paw.interfaces.PlaceService;
import ar.edu.itba.paw.interfaces.TripMemberService;
import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.exception.ActivityNotPartOfTheTripException;
import ar.edu.itba.paw.model.exception.EntityNotFoundException;
import ar.edu.itba.paw.model.exception.InvalidDateRangeException;
import ar.edu.itba.paw.model.exception.UserNotOwnerOrAdminException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService
{

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private TripMemberService tripMemberService;

    @Autowired
    private TripService tripService;

    @Override
    public Optional<Activity> findById( long id ) {
        return activityDao.findById( id );
    }

    @Override
    public Activity create( long tripId, String name, LocalDate startDate, LocalDate endDate, Place place,
                            String username )
            throws EntityNotFoundException, UserNotOwnerOrAdminException, InvalidDateRangeException {
        Optional<Trip> maybeTrip = tripService.findById( tripId );

        if ( !maybeTrip.isPresent() ) {
            throw new EntityNotFoundException();
        }

        if ( !tripMemberService.isUserOwnerOrAdmin( tripId, username ) ) {
            throw new UserNotOwnerOrAdminException();
        }

        if ( !areValidDates( maybeTrip.get(), startDate, endDate ) ) {
            throw new InvalidDateRangeException();
        }

        place = placeService.createIfNotExists( place.getGoogleId(), place.getName(), place.getLatitude(),
                                                place.getLongitude(), place.getAddress() );

        return activityDao.create( name, maybeTrip.get(), startDate, endDate, place );
    }

    @Override
    public List<Activity> findByTrip( long tripId ) {
        return activityDao.getTripActivities( tripId );
    }

    @Override
    public boolean isActivityPartOfTheTrip( long tripId, long activityId ) {
        return activityDao.isActivityPartOfTheTrip( tripId, activityId );
    }

    @Override
    public Activity update( long id, long tripId, String name, LocalDate startDate, LocalDate endDate, Place place,
                            String username )
            throws EntityNotFoundException, UserNotOwnerOrAdminException, InvalidDateRangeException,
            ActivityNotPartOfTheTripException {
        Optional<Trip> maybeTrip = tripService.findById( tripId );
        Optional<Activity> maybeActivity = findById( id );

        if ( !maybeTrip.isPresent() || !maybeActivity.isPresent() ) {
            throw new EntityNotFoundException();
        }

        if ( !isActivityPartOfTheTrip( tripId, id ) ) {
            throw new ActivityNotPartOfTheTripException();
        }

        if ( !tripMemberService.isUserOwnerOrAdmin( tripId, username ) ) {
            throw new UserNotOwnerOrAdminException();
        }

        if ( !areValidDates( maybeTrip.get(), startDate, endDate ) ) {
            throw new InvalidDateRangeException();
        }

        Activity activity = maybeActivity.get();

        place = placeService.createIfNotExists( place.getGoogleId(), place.getName(), place.getLatitude(),
                                                place.getLongitude(), place.getAddress() );

        activity.setTrip( maybeTrip.get() );
        activity.setName( name );
        activity.setPlace( place );
        activity.setStartDate( startDate );
        activity.setEndDate( endDate );

        return activityDao.update( activity );
    }

    @Override
    public void delete( long id, long tripId, String username )
            throws EntityNotFoundException, ActivityNotPartOfTheTripException, UserNotOwnerOrAdminException {
        Optional<Activity> maybeActivity = findById( id );

        if ( !maybeActivity.isPresent() ) {
            throw new EntityNotFoundException();
        }

        if ( !isActivityPartOfTheTrip( tripId, id ) ) {
            throw new ActivityNotPartOfTheTripException();
        }

        if ( !tripMemberService.isUserOwnerOrAdmin( tripId, username ) ) {
            throw new UserNotOwnerOrAdminException();
        }

        activityDao.deleteActivity( id );
    }

    private boolean areValidDates( Trip trip, LocalDate startDate, LocalDate endDate ) {
        return isValidStartDate( trip, startDate ) && isValidEndDate( trip, endDate ) &&
               areValidDates( startDate, endDate );
    }

    private boolean isValidStartDate( Trip trip, LocalDate startDate ) {
        return trip.getStartDate().isEqual( startDate ) || trip.getStartDate().isBefore( startDate );
    }

    private boolean isValidEndDate( Trip trip, LocalDate endDate ) {
        return trip.getEndDate().isEqual( endDate ) || trip.getEndDate().isAfter( endDate );
    }

    private boolean areValidDates( LocalDate startDate, LocalDate endDate ) {
        return startDate.isEqual( endDate ) || startDate.isBefore( endDate );
    }
}
