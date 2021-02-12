package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.DataPair;
import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Trip;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ActivityService {
    Optional<Activity> findById( long id );
    Optional<Activity> findByName( String name );
    Activity create( String name, Place place, Trip trip, LocalDate startDate, LocalDate endDate );
    List<Activity> findByTrip( long tripId );
    boolean isActivityPartOfTheTrip(long tripId, long activityId);
    Activity update(Activity activity);
}
