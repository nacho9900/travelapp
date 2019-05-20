package ar.edu.itba.paw.model;

public class TripPicture {

    private final long id;
    private byte[] picture;
    private final long tripId;

    public TripPicture(long id, byte[] picture, long tripId) {
        this.id = id;
        this.picture = picture;
        this.tripId = tripId;
    }

    public long getId() {
        return id;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public long getTripId() {
        return tripId;
    }
}
