package ar.edu.itba.paw.model;

public class ActivityPlace {

    private final long id;
    private final long activityId;
    private final long placeId;

    public ActivityPlace(long id, long activityId, long placeId) {
        this.id = id;
        this.activityId = activityId;
        this.placeId = placeId;
    }

    public long getId() {
        return id;
    }

    public long getActivityId() {
        return activityId;
    }

    public long getPlaceId() {
        return placeId;
    }
}
