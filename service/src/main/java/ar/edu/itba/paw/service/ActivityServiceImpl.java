package ar.edu.itba.paw.service;


import ar.edu.itba.paw.interfaces.ActivityDao;
import ar.edu.itba.paw.interfaces.ActivityService;
import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.DataPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
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
    public Activity create(String name, long categoryId) {
        return ad.create(name, categoryId);
    }

    @Override
    public List<Activity> getTripActivities(long tripId) {
        return ad.getTripActivities(tripId);
    }


    public List<DataPair<Activity, DataPair<List<String>, List<String>>>> getTripActivitiesAndCategories(long tripId) {

        List<DataPair<Activity, DataPair<List<String>, List<String>>>> listDataPair = new LinkedList<>();
        List<Activity> activities = ad.getTripActivities(tripId);
        for(Activity activity : activities) {
            long aId = activity.getId();
            listDataPair.add(new DataPair<>(activity, new DataPair<>(ad.getActivityPlaces(aId), ad.getActivityCategories(aId))));
        }
        return listDataPair;
    }
}
