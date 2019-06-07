package ar.edu.itba.paw.model;


import javax.persistence.*;

@Entity
@Table(name = "trip_places")
public class TripPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_places_id_seq")
    @SequenceGenerator(sequenceName = "trip_places_id_seq", name = "trip_places_id_seq", allocationSize = 1)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "place_id")
    private Place place;

    public TripPlace(long id, Trip trip, Place place) {
        this(trip, place);
        this.id = id;
    }

    public TripPlace(Trip trip, Place place) {
        this.trip = trip;
        this.place = place;
    }

    /* package */ TripPlace() {
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
}
