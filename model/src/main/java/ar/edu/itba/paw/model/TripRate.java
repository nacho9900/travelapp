package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "trip_rate")
public class TripRate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_comments_id_seq")
    @SequenceGenerator(sequenceName = "trip_comments_id_seq", name = "trip_comments_id_seq", allocationSize = 1)
    private long id;

    public TripRate(int rate, LocalDateTime createdOn, TripMember member)
    {
        this.rate = rate;
        this.createdOn = createdOn;
        this.member = member;
    }

    @Column(nullable = false)
    private int rate;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    ////////////

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private TripMember member;

    ////////////

    protected TripRate() {
        // Just for Hibernate
    }

    public long getId() {
        return id;
    }

    public int getRate() {
        return rate;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public TripMember getMember() {
        return member;
    }
}
