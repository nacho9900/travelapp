package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_id_seq")
    @SequenceGenerator(sequenceName = "trip_id_seq", name = "trip_id_seq", allocationSize = 1)
    private long id;

    @Column
    private long startPlaceId;

    @Column
    private long adminId;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 500, nullable = false)
    private String description;

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Calendar startDate;

    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Calendar endDate;


    /////////////////


    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> users = new LinkedList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "trip")
    private List<Activity> activities = new LinkedList<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "trip")
    private TripPicture profilePicture;



    /////////////////


    public Trip(long id, long adminId, long startPlaceId, String name, String description, Calendar startDate, Calendar endDate) {
        this(adminId, startPlaceId, name, description, startDate, endDate);
        this.id = id;
    }

    public Trip(long adminId, long startPlaceId, String name, String description, Calendar startDate, Calendar endDate) {
        super();
        this.adminId = adminId;
        this.startPlaceId = startPlaceId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    /* package */ Trip() {
        // Just for Hibernate
    }

    public long getStartPlaceId() {
        return startPlaceId;
    }

    public void setStartPlaceId(long startPlaceId) {
        this.startPlaceId = startPlaceId;
    }

    public long getId() {
        return id;
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
