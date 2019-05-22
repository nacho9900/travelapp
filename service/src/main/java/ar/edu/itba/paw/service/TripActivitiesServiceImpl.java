package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.TripActivitiesDao;
import ar.edu.itba.paw.interfaces.TripActivitiesService;
import ar.edu.itba.paw.model.TripActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TripActivitiesServiceImpl implements TripActivitiesService {

    @Autowired
    TripActivitiesDao td;

    @Override
    public TripActivity create(long tripId, long placeId, long activityId) {
        return td.create(tripId, placeId, activityId);
    }

    @Override
    public Optional<TripActivity> findById(long id) {
        return td.findById(id);
    }
}
