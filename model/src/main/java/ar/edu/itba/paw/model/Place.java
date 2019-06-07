package ar.edu.itba.paw.model;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "places")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "places_id_seq")
    @SequenceGenerator(sequenceName = "places_id_seq", name = "places_id_seq", allocationSize = 1)
    private long id;

    @Column(length = 150, name = "google_id", nullable = false)
    private String googleId;

    @Column(length = 100, nullable = false)
    private String name;

    @Column
    private double latitude;

    @Column
    private double longitude;

    @Column(length = 500, nullable = false)
    private String address;

    ////////////////

    @OneToMany(fetch = FetchType.LAZY)
    private List<Activity> activities;

    @ManyToOne(fetch = FetchType.LAZY)
    private List<Trip> trips;

    ////////////////

    public Place(long id, String googleId, String name, double latitude, double longitude, String address) {
        this(googleId, name, latitude, longitude, address);
        this.id = id;
    }

    public Place(String googleId, String name, double latitude, double longitude, String address) {
        this.googleId = googleId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    /* package */ Place() {
        // Just for Hibernate
    }

    public long getId() {
        return id;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
