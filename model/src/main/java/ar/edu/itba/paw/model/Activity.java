package ar.edu.itba.paw.model;


import javax.persistence.*;

@Entity
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_id_seq")
    @SequenceGenerator(sequenceName = "activity_id_seq", name = "activity_id_seq", allocationSize = 1)
    private long id;

    @Column(length = 40, nullable = false)
    private String name;

    @Column(length = 40, nullable = false)
    private String category;


    ///////////////

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Place place;


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Trip trip;

    //////////////



    public Activity(long id, String name, String category, Place place, Trip trip) {
        this(name, category, place, trip);
        this.id = id;
    }

    public Activity(String name, String category, Place place, Trip trip) {
        this.name = name;
        this.category = category;
        this.place = place;
        this.trip = trip;
    }

    /* package */ Activity() {
        // Just for Hibernate
    }


    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
