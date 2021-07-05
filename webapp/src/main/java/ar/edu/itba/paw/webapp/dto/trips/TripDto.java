package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.webapp.controller.TripController;
import ar.edu.itba.paw.webapp.dto.validators.Future;
import ar.edu.itba.paw.webapp.dto.validators.TripConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;

@TripConstraint
@JsonInclude( JsonInclude.Include.NON_NULL )
public class TripDto
{
    private Long id;
    @NotBlank
    @Length( max = 100 )
    private String name;
    @NotBlank
    @Length( max = 500 )
    private String description;
    @NotNull
    @Future
    private LocalDate startDate;
    @NotNull
    @Future
    private LocalDate endDate;
    private boolean containActivities;
    private URI tripUri;
    private URI tripCommentsUri;
    private URI tripActivitiesUri;
    private URI tripMembersUri;
    private URI tripJoinRequestUri;
    private URI tripPictureUri;

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

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public URI getTripUri() {
        return tripUri;
    }

    public URI getTripCommentsUri() {
        return tripCommentsUri;
    }

    public URI getTripActivitiesUri() {
        return tripActivitiesUri;
    }

    public URI getTripMembersUri() {
        return tripMembersUri;
    }

    public URI getTripJoinRequestUri() {
        return tripJoinRequestUri;
    }

    public URI getTripPictureUri() {
        return tripPictureUri;
    }

    public boolean isContainActivities() {
        return containActivities;
    }

    public void setContainActivities( boolean containActivities ) {
        this.containActivities = containActivities;
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

    public void setStartDate( LocalDate startDate ) {
        this.startDate = startDate;
    }

    public void setEndDate( LocalDate endDate ) {
        this.endDate = endDate;
    }

    public Trip toTrip() {
        LocalDate startDate = this.startDate;

        LocalDate endDate = this.endDate;

        Trip trip;

        if ( this.id == null ) {
            trip = new Trip( 0, this.name, this.description, startDate, endDate );
        }
        else {
            trip = new Trip( this.id, this.name, this.description, startDate, endDate );
        }

        return trip;
    }

    public static TripDto fromTrip( Trip trip, UriInfo uriInfo ) {
        TripDto tripDto = new TripDto();
        tripDto.id = trip.getId();
        tripDto.name = trip.getName();
        tripDto.startDate = trip.getStartDate();
        tripDto.endDate = trip.getEndDate();
        tripDto.description = trip.getDescription();
        tripDto.containActivities = trip.getActivities().size() > 0;
        tripDto.tripUri = uriInfo.getBaseUriBuilder()
                                 .path( TripController.class )
                                 .path( TripController.class, "get" )
                                 .build( trip.getId() );
        tripDto.tripCommentsUri = uriInfo.getBaseUriBuilder()
                                         .path( TripController.class )
                                         .path( TripController.class, "getAllComments" )
                                         .build( trip.getId() );
        tripDto.tripActivitiesUri = uriInfo.getBaseUriBuilder()
                                           .path( TripController.class )
                                           .path( TripController.class, "getActivities" )
                                           .build( trip.getId() );
        tripDto.tripMembersUri = uriInfo.getBaseUriBuilder()
                                        .path( TripController.class )
                                        .path( TripController.class, "getAllMembers" )
                                        .build( trip.getId() );
        tripDto.tripJoinRequestUri = uriInfo.getBaseUriBuilder()
                                            .path( TripController.class )
                                            .path( TripController.class, "getAllJoinRequest" )
                                            .build( trip.getId() );
        tripDto.tripPictureUri = uriInfo.getBaseUriBuilder()
                                        .path( TripController.class )
                                        .path( TripController.class, "getPicture" )
                                        .build( trip.getId() );

        return tripDto;
    }
}
