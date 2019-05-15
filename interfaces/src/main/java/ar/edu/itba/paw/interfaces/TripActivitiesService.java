package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.TripActivity;

import java.util.Optional;

public interface TripActivitiesService {

    public TripActivity create(long tripId, long placeId, long activityId);
    public Optional<TripActivity> findById(long id);
}
