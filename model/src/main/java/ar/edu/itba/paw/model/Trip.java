package ar.edu.itba.paw.model;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trips_id_seq")
    @SequenceGenerator(sequenceName = "trips_id_seq", name = "trip_id_seq", allocationSize = 1)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "startplace_id")
    private Place startPlace;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 500, nullable = false)
    private String description;

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar startDate;

    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar endDate;

    /////////////////

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Place> places;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> users;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Activity> activities;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "trip")
    private TripPicture profilePicture;

    /////////////////

    public Trip(long id, Place startPlace, String name, String description, Calendar startDate, Calendar endDate) {
        this(startPlace, name, description, startDate, endDate);
        this.id = id;
    }

    public Trip(Place startPlace, String name, String description, Calendar startDate, Calendar endDate) {
        super();
        this.startPlace = startPlace;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /* package */ Trip() {
        // Just for Hibernate
    }

    public long getId() {
        return id;
    }

    public Place getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(Place startPlace) {
        this.startPlace = startPlace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public TripPicture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(TripPicture profilePicture) {
        this.profilePicture = profilePicture;
    }
}
