package ar.edu.itba.paw.service;


import ar.edu.itba.paw.interfaces.ActivityDao;
import ar.edu.itba.paw.interfaces.ActivityService;
import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.DataPair;
import ar.edu.itba.paw.model.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    ActivityDao ad;

    @Override
    public Optional<Activity> findById(long id) {
        return ad.findById(id);
    }

    @Override
    public Optional<Activity> findByName(String name) {
        return ad.findByName(name);
    }

    @Override
    public Activity create(String name, String category, long placeId) {
        return ad.create(name, category, placeId);
    }

    @Override
    public List<Activity> getTripActivities(long tripId) {
        return ad.getTripActivities(tripId);
    }

    @Override
    public List<DataPair<Activity, Place>> getTripActivitiesDetails(long tripId) {
        List<Activity> activities = ad.getTripActivities(tripId);
        List<DataPair<Activity, Place>> dataPairList = new ArrayList<>();
        for(Activity activity : activities) {
            Optional<Place> optionalPlace = ad.getActivityPlace(activity.getId());
            optionalPlace.ifPresent(place -> dataPairList.add(new DataPair<>(activity, place)));

        }
        return dataPairList;
    }

    @Override
    public Optional<Activity> findByCategory(String category) {
        return ad.findByCategory(category);
    }

}
