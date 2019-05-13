package ar.edu.itba.paw.model;

public class Place {

    private final long id;
    private final String googleId;
    private final String name;
    private final double latitude;
    private final double longitude;
    private final String address;


    public Place(long id, String googleId, String name, double latitude, double longitude, String address) {
        this.id = id;
        this.googleId = googleId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public String getGoogleId() {
        return googleId;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }
}
