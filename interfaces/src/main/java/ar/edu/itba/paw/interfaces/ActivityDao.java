package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Activity;

import java.util.List;
import java.util.Optional;

public interface ActivityDao {

    public Optional<Activity> findById(long id);
    public Optional<Activity> findByName(String name);
    public Activity create(String name, long categoryId);
    public List<Activity> getTripActivities(long tripId);
    public List<String> getActivityCategories(long id);
    public List<String> getActivityPlaces(long id);
}
