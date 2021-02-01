package ar.edu.itba.paw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
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

    ////////////

    @ManyToOne( fetch = FetchType.LAZY, optional = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;

    ////////////

    protected TripJoinRequest() {
        // Hibernate
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

    public long getId() {
        return id;
    }
}
