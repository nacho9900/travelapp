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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table( name = "trip_members")
public class TripMember
{
    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "trip_members_id_seq")
    @SequenceGenerator( sequenceName = "trip_members_id_seq", name = "trip_members_id_seq", allocationSize = 1)
    private long id;

    @Enumerated( EnumType.STRING)
    @Column(name = "role", nullable = false)
    private TripMemberRole role;

    @Column(name = "is_active")
    private Boolean isActive;

    ////////////

    @ManyToOne( fetch = FetchType.LAZY, optional = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;

    @OneToOne( fetch = FetchType.EAGER, mappedBy = "member")
    private TripRate rate;

    @OneToMany( fetch = FetchType.EAGER, mappedBy = "member")
    private List<TripComment> comments = new LinkedList<>();

    ////////////

    protected TripMember() {
        //Hibernate
    }

    public long getId() {
        return id;
    }

    public TripMemberRole getRole() {
        return role;
    }

    public void setRole( TripMemberRole role ) {
        this.role = role;
    }

    public Trip getTrip() {
        return trip;
    }

    public User getUser() {
        return user;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive( Boolean active ) {
        isActive = active;
    }

    public List<TripComment> getComments() {
        return comments;
    }

    public void setComments( List<TripComment> comments ) {
        this.comments = comments;
    }

    public TripRate getRate() {
        return rate;
    }

    public void setRate( TripRate rate ) {
        this.rate = rate;
    }
}
