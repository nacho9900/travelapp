package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.DataPair;
import ar.edu.itba.paw.model.Place;

import java.util.List;
import java.util.Optional;

public interface ActivityService {
    public Optional<Activity> findById(long id);
    public Optional<Activity> findByName(String name);
    public Activity create(String name, String category, long placeId);
    public List<Activity> getTripActivities(long tripId);
    public Optional<Activity> findByCategory(String category);
    public List<DataPair<Activity, Place>> getTripActivitiesDetails(long tripId);
}
