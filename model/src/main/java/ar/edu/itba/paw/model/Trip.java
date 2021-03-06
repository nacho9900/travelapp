package ar.edu.itba.paw.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table( name = "trips" )
public class Trip implements Comparable<Trip>
{
    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE,
                     generator = "trip_id_seq" )
    @SequenceGenerator( sequenceName = "trip_id_seq",
                        name = "trip_id_seq",
                        allocationSize = 1 )
    private long id;

    @Column( length = 100,
             nullable = false )
    private String name;

    @Column( length = 500,
             nullable = false )
    private String description;

    @Column( name = "start_date",
             nullable = false )
    private LocalDate startDate;

    @Column( name = "end_date",
             nullable = false )
    private LocalDate endDate;

    /////////////////

    @OneToMany( fetch = FetchType.LAZY,
                mappedBy = "trip",
                cascade = {CascadeType.REMOVE, CascadeType.ALL},
                orphanRemoval = true )
    private List<Activity> activities = new LinkedList<>();

    @OneToOne( fetch = FetchType.LAZY,
               mappedBy = "trip",
               cascade = CascadeType.ALL,
               orphanRemoval = true )
    private TripPicture profilePicture;

    @OneToMany( fetch = FetchType.LAZY,
                cascade = CascadeType.ALL,
                mappedBy = "trip",
                orphanRemoval = true )
    private List<TripJoinRequest> joinRequests = new LinkedList<>();

    @OneToMany( fetch = FetchType.LAZY,
                mappedBy = "trip",
                cascade = CascadeType.ALL,
                orphanRemoval = true )
    private List<TripMember> members = new LinkedList<>();

    /////////////////


    public Trip( long id, String name, String description, LocalDate startDate, LocalDate endDate ) {
        this( name, description, startDate, endDate );
        this.id = id;
    }

    public Trip( String name, String description, LocalDate startDate, LocalDate endDate ) {
        super();
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    protected Trip() {
        // Just for Hibernate
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate( LocalDate startDate ) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate( LocalDate endDate ) {
        this.endDate = endDate;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities( List<Activity> activities ) {
        for ( Activity activity : activities ) {
            activity.setTrip( this );
        }
        this.activities = activities;
    }

    public TripPicture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture( TripPicture profilePicture ) {
        profilePicture.setTrip( this );
        this.profilePicture = profilePicture;
    }

    public List<TripJoinRequest> getJoinRequests() {
        return joinRequests;
    }

    public void setJoinRequests( List<TripJoinRequest> joinRequests ) {
        for ( TripJoinRequest tripJoinRequest : joinRequests ) {
            tripJoinRequest.setTrip( this );
        }
        this.joinRequests = joinRequests;
    }

    public List<TripMember> getMembers() {
        return members;
    }

    public void setMembers( List<TripMember> members ) {
        for ( TripMember member : members ) {
            member.setTrip( this );
        }

        this.members = members;
    }

    @Override
    public int compareTo( Trip o ) {
        return ( this.startDate.isBefore( o.startDate ) ) ? -1 : 1;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }

        Trip trip = (Trip) o;

        return id == trip.id;
    }

    @Override
    public int hashCode() {
        return (int) ( id ^ ( id >>> 32 ) );
    }
}
