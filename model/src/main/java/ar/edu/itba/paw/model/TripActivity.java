package ar.edu.itba.paw.model;


import javax.persistence.*;

@Entity
@Table(name = "trip_activities")
public class TripActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_activities_id_seq")
    @SequenceGenerator(sequenceName = "trip_activities_id_seq", name = "trip_activities_id_seq", allocationSize = 1)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "place_id")
    private Place place;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "activity_id")
    private Activity activity;

    public TripActivity(long id, Trip trip, Place place, Activity activity) {
        this(trip, place, activity);
        this.id = id;
    }

    public TripActivity(Trip trip, Place place, Activity activity) {
        this.place = place;
        this.trip = trip;
        this.activity = activity;
    }

    /* package */ TripActivity() {
        // Just for Hibernate
    }

    public long getId() {
        return id;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
