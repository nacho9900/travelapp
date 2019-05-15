package ar.edu.itba.paw.model;

public class TripActivity {

    private final long id;
    private final long tripId;
    private final long placeId;
    private final long activityId;

    public TripActivity(long id, long tripId, long placeId, long activityId) {
        this.id = id;
        this.tripId = tripId;
        this.placeId = placeId;
        this.activityId = activityId;
    }

    public long getId() {
        return id;
    }

    public long getTripId() {
        return tripId;
    }

    public long getPlaceId() {
        return placeId;
    }

    public long getActivityId() {
        return activityId;
    }
}
