package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripJoinRequest;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.webapp.dto.serializers.CollectionSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.cglib.core.Local;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TripDto
{
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    @Future
    private Date startDate;
    @NotNull
    @Future
    private Date endDate;
    private URI tripPicture;
    @JsonSerialize( using = CollectionSerializer.class )
    private List<TripMemberDto> members;
    @JsonSerialize( using = CollectionSerializer.class )
    private List<ActivityDto> activities;
    @JsonSerialize( using = CollectionSerializer.class )
    private List<JoinRequestDto> joinRequests;

    public TripDto() {
        //For Jackson
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public URI getTripPicture() {
        return tripPicture;
    }

    public List<TripMemberDto> getMembers() {
        return members;
    }

    public List<ActivityDto> getActivities() {
        return activities;
    }

    public List<JoinRequestDto> getJoinRequests() {
        return joinRequests;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public void setStartDate( Date startDate ) {
        this.startDate = startDate;
    }

    public void setEndDate( Date endDate ) {
        this.endDate = endDate;
    }

    public void setTripPicture( URI tripPicture ) {
        this.tripPicture = tripPicture;
    }

    public void setMembers( List<TripMemberDto> members ) {
        this.members = members;
    }

    public void setActivities( List<ActivityDto> activities ) {
        this.activities = activities;
    }

    public void setJoinRequests( List<JoinRequestDto> joinRequests ) {
        this.joinRequests = joinRequests;
    }

    public Trip toTrip() {
        LocalDate startDate = this.startDate.toInstant()
                                            .atZone( ZoneId.systemDefault() )
                                            .toLocalDate();

        LocalDate endDate = this.endDate.toInstant()
                                        .atZone( ZoneId.systemDefault() )
                                        .toLocalDate();

        Trip trip;

        if(this.id == null) {
            trip = new Trip( 0, this.name, this.description, startDate, endDate );
        } else {
            trip = new Trip( this.id, 0, this.name, this.description, startDate, endDate );
        }

        if ( this.joinRequests != null ) {
            trip.setJoinRequests( this.joinRequests.stream()
                                                   .map( JoinRequestDto::toTripJoinRequest )
                                                   .collect( Collectors.toList() ) );
        }

        if ( this.activities != null ) {
            trip.setActivities( this.activities.stream()
                                               .map( ActivityDto::toActivity )
                                               .collect( Collectors.toList() ) );
        }

        if ( this.members != null ) {
            trip.setMembers( this.members.stream()
                                         .map( TripMemberDto::toTripMember )
                                         .collect( Collectors.toList() ) );
        }

        return trip;
    }

    public static TripDto fromTrip( Trip trip, UriInfo uriInfo, boolean includeMembers, boolean includeActivities,
                                    boolean includeJoinRequests ) {
        TripDto tripDto = new TripDto();
        tripDto.id = trip.getId();
        tripDto.name = trip.getName();
        tripDto.startDate = java.sql.Date.valueOf( trip.getStartDate() );
        tripDto.endDate = java.sql.Date.valueOf( trip.getEndDate() );

        if ( includeMembers ) {
            List<TripMember> tripMembers = trip.getMembers();
            if ( tripMembers != null ) {
                tripDto.members = tripMembers.stream()
                                             .map( x -> TripMemberDto.fromTripMember( x, uriInfo, true, true ) )
                                             .collect( Collectors.toList() );
            }
        }

        if ( includeActivities ) {
            List<Activity> activities = trip.getActivities();
            if ( activities != null ) {
                tripDto.activities = activities.stream()
                                               .map( ActivityDto::fromActivity )
                                               .collect( Collectors.toList() );
            }
        }

        if ( includeJoinRequests ) {
            List<TripJoinRequest> tripJoinRequests = trip.getJoinRequests();
            if ( tripJoinRequests != null ) {
                tripDto.joinRequests = tripJoinRequests.stream()
                                                       .map( x -> JoinRequestDto.fromTripJoinRequest( x, uriInfo ) )
                                                       .collect( Collectors.toList() );
            }
        }

        return tripDto;
    }
}
