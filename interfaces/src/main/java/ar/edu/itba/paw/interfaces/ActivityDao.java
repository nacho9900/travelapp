package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Trip;

import java.util.List;
import java.util.Optional;

public interface ActivityDao {

    public Optional<Activity> findById(long id);
    public Optional<Activity> findByName(String name);
    public Activity create(String name, String category, Place place, Trip trip);
    public Optional<Place> getActivityPlace(long id);
    public Optional<Activity> findByCategory(String category);

}
