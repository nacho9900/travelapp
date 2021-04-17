package ar.edu.itba.paw.service;


import ar.edu.itba.paw.interfaces.ActivityDao;
import ar.edu.itba.paw.interfaces.ActivityService;
import ar.edu.itba.paw.interfaces.PlaceService;
import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    ActivityDao activityDao;

    @Autowired
    PlaceService placeService;

    @Override
    public Optional<Activity> findById(long id) {
        return activityDao.findById(id);
    }

    @Override
    public Activity create( String name, Trip trip, LocalDate startDate, LocalDate endDate, Place place ) {
        place = placeService.createIfNotExists( place.getGoogleId(), place.getName(),
                                                place.getLatitude(), place.getLongitude(),
                                                place.getAddress() );

        return activityDao.create( name, trip, startDate, endDate, place );
    }

    @Override
    public List<Activity> findByTrip(long tripId) {
        return activityDao.getTripActivities( tripId );
    }

    @Override
    public boolean isActivityPartOfTheTrip( long tripId, long activityId ) {
        return activityDao.isActivityPartOfTheTrip( tripId, activityId );
    }

    @Override
    public Activity update( Activity activity ) {
        Place place = activity.getPlace();
        place = placeService.createIfNotExists( place.getGoogleId(), place.getName(),
                                                place.getLatitude(), place.getLongitude(),
                                                place.getAddress() );
        activity.setPlace( place );
        return activityDao.update( activity );
    }

    @Override
    public void delete( long id ) {
        activityDao.deleteActivity( id );
    }
}
