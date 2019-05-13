package ar.edu.itba.paw.model;

public class TripPlace {

    private final long id;
    private final long tripId;
    private final long placeId;

    public TripPlace(long id, long tripId, long placeId) {
        this.id = id;
        this.tripId = tripId;
        this.placeId = placeId;
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
}
