package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.DataPair;

import java.util.List;
import java.util.Optional;

public interface ActivityService {

    public Optional<Activity> findById(long id);
    public Optional<Activity> findByName(String name);
    public Activity create(String name, long categoryId);
    public List<Activity> getTripActivities(long tripId);
    public List<DataPair<Activity, List<String>>> getTripActivitiesAndCategories(long tripId);
}
