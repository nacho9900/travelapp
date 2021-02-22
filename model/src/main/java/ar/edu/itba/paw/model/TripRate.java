package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "trip_rate")
public class TripRate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_rate_id_seq")
    @SequenceGenerator(sequenceName = "trip_rate_id_seq", name = "trip_rate_id_seq", allocationSize = 1)
    private long id;

    @Column(nullable = false)
    private int rate;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    ////////////

    @OneToOne( fetch = FetchType.LAZY, optional = false)
    private TripMember member;

    ////////////

    protected TripRate() {
        // Just for Hibernate
    }

    public TripRate(long id, int rate, LocalDateTime createdOn, TripMember member) {
        this(rate, createdOn, member);
        this.id = id;
    }

    public TripRate(int rate, LocalDateTime createdOn, TripMember member)
    {
        this(rate, createdOn);
        this.member = member;
    }

    public TripRate(int rate, LocalDateTime createdOn) {
        this.rate = rate;
        this.createdOn = createdOn;
    }

    public long getId() {
        return id;
    }

    public int getRate() {
        return rate;
    }

    public void setRate( int rate ) {
        this.rate = rate;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn( LocalDateTime createdOn ) {
        this.createdOn = createdOn;
    }

    public TripMember getMember() {
        return member;
    }

    public void setMember( TripMember member ) {
        this.member = member;
    }
}
