package ar.edu.itba.paw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "trip_join_requests")
public class TripJoinRequest
{
    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "trip_join_requests_id_seq")
    @SequenceGenerator( sequenceName = "trip_join_requests_id_seq", name = "trip_join_requests_id_seq", allocationSize = 1)
    private long id;

    @Column( name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name ="message", nullable = true)
    private String message;

    @Enumerated( EnumType.STRING)
    @Column(name="status", nullable = false)
    private TripJoinRequestStatus status;

    ////////////

    @ManyToOne( fetch = FetchType.LAZY, optional = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;

    ////////////

    protected TripJoinRequest() {
        // Hibernate
    }

    public TripJoinRequest( long id, LocalDateTime createdOn, String message, TripJoinRequestStatus status, Trip trip
            , User user ) {
        this.id = id;
        this.createdOn = createdOn;
        this.message = message;
        this.status = status;
        this.trip = trip;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn( LocalDateTime createdOn ) {
        this.createdOn = createdOn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage( String message ) {
        this.message = message;
    }

    public TripJoinRequestStatus getStatus() {
        return status;
    }

    public void setStatus( TripJoinRequestStatus status ) {
        this.status = status;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip( Trip trip ) {
        this.trip = trip;
    }

    public User getUser() {
        return user;
    }

    public void setUser( User user ) {
        this.user = user;
    }
}
