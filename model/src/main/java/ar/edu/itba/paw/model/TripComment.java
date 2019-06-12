package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "trip_comments")
public class TripComment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_comments_id_seq")
    @SequenceGenerator(sequenceName = "trip_comments_id_seq", name = "trip_comments_id_seq", allocationSize = 1)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="trip_id")
    private Trip trip;

    @Column(length = 160, nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_on", nullable = false)
    private LocalDate createdOn;

    /* package */ TripComment() {
        // Just for Hibernate
    }

    public TripComment(Trip trip, String comment, User user, LocalDate createdOn) {
        this.trip = trip;
        this.comment = comment;
        this.user = user;
        this.createdOn = createdOn;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }
}
