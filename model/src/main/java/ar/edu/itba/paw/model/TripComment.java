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
@Table( name = "trip_comments" )
public class TripComment implements Comparable<TripComment>
{
    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE,
                     generator = "trip_comments_id_seq" )
    @SequenceGenerator( sequenceName = "trip_comments_id_seq",
                        name = "trip_comments_id_seq",
                        allocationSize = 1 )
    private long id;

    @Column( length = 160,
             nullable = false )
    private String comment;

    @Column( name = "created_on",
             nullable = false )
    private LocalDateTime createdOn;

    ////////////

    @ManyToOne( fetch = FetchType.LAZY,
                optional = false )
    private TripMember member;

    ////////////

    protected TripComment() {
        // Just for Hibernate
    }

    public TripComment(long id, TripMember member, String comment, LocalDateTime createdOn) {
        this(member, comment, createdOn);
        this.id = id;
    }

    public TripComment( TripMember member, String comment, LocalDateTime createdOn ) {
        this.comment = comment;
        this.createdOn = createdOn;
        this.setMember( member );
    }

    public long getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment( String comment ) {
        this.comment = comment;
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

    @Override
    public int compareTo( TripComment o ) {
        return this.createdOn.isBefore( o.createdOn ) ? -1 : 1;
    }

}
