package ar.edu.itba.paw.model;

import javax.persistence.CascadeType;
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
@Table( name = "trip_members" )
public class TripMember
{
    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE,
                     generator = "trip_members_id_seq" )
    @SequenceGenerator( sequenceName = "trip_members_id_seq",
                        name = "trip_members_id_seq",
                        allocationSize = 1 )
    private long id;

    @Enumerated( EnumType.STRING )
    @Column( name = "role",
             nullable = false )
    private TripMemberRole role;

    @Column( name = "is_active" )
    private Boolean isActive;

    ////////////

    @ManyToOne
    private Trip trip;

    @ManyToOne( fetch = FetchType.EAGER,
                optional = false )
    private User user;

    @OneToOne( fetch = FetchType.EAGER,
               mappedBy = "member",
               cascade = CascadeType.ALL )
    private TripRate rate;

    @OneToMany( fetch = FetchType.EAGER,
                mappedBy = "member",
                cascade = CascadeType.ALL )
    private List<TripComment> comments = new LinkedList<>();

    ////////////

    protected TripMember() {
        //Hibernate
    }

    public TripMember( long id, Trip trip, TripMemberRole role, Boolean isActive, User user, TripRate rate,
                       List<TripComment> comments ) {
        this( trip, role, isActive, user, rate, comments );
        this.id = id;
    }

    public TripMember( TripMemberRole role, Boolean isActive, User user, TripRate rate ) {
        this.role = role;
        this.isActive = isActive;
        this.user = user;
        this.rate = rate;
    }

    public TripMember( Trip trip, TripMemberRole role, Boolean isActive, User user, TripRate rate,
                       List<TripComment> comments ) {
        this( role, isActive, user, rate );
        this.trip = trip;
        this.comments = comments;
    }

    public TripMember(TripMemberRole role, Boolean isActive, User user) {
        this.role = role;
        this.isActive = isActive;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public TripMemberRole getRole() {
        return role;
    }

    public void setRole( TripMemberRole role ) {
        this.role = role;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive( Boolean active ) {
        isActive = active;
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

    public TripRate getRate() {
        return rate;
    }

    public void setRate( TripRate rate ) {
        rate.setMember( this );
        this.rate = rate;
    }

    public List<TripComment> getComments() {
        return comments;
    }

    public void setComments( List<TripComment> comments ) {
        for(TripComment comment : comments) {
            comment.setMember( this );
        }
        this.comments = comments;
    }
}
