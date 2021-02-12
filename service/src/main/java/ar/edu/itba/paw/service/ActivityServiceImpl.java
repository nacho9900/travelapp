package ar.edu.itba.paw.service;


import ar.edu.itba.paw.interfaces.ActivityDao;
import ar.edu.itba.paw.interfaces.ActivityService;
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

    @Override
    public Optional<Activity> findById(long id) {
        return activityDao.findById(id);
    }

    @Override
    public Optional<Activity> findByName(String name) {
        return activityDao.findByName(name);
    }

    @Override
    public Activity create(String name, Place place, Trip trip, LocalDate startDate, LocalDate endDate) {
        return activityDao.create(name, place, trip, startDate, endDate);
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
        return activityDao.update( activity );
    }

    @Override
    public void delete( long id ) {
        activityDao.deleteActivity( id );
    }
}
