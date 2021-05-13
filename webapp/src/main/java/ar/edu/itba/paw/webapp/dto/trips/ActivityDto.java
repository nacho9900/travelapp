package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.webapp.controller.TripController;
import ar.edu.itba.paw.webapp.dto.validators.Future;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;

@JsonInclude( JsonInclude.Include.NON_NULL )
public class ActivityDto
{
    private Long id;
    @NotBlank
    @Length( max = 40 )
    private String name;
    @NotNull
    @Future
    private LocalDate startDate;
    @NotNull
    @Future
    private LocalDate endDate;
    @NotNull
    private PlaceDto place;
    private URI activityUri;

    public ActivityDto() {
        //For Jackson
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public PlaceDto getPlace() {
        return this.place;
    }

    public URI getActivityUri() {
        return activityUri;
    }

    public Activity toActivity() {
        LocalDate startDate = this.startDate;
        LocalDate endDate = this.endDate;

        if ( this.id != null ) {
            return new Activity( this.id, this.name, this.place.toPlace(), null, startDate, endDate );
        }
        else {
            return new Activity( this.name, this.place.toPlace(), null, startDate, endDate );
        }
    }

    public static ActivityDto fromActivity( Activity activity, UriInfo uriInfo, long tripId ) {
        ActivityDto activityDto = new ActivityDto();
        activityDto.id = activity.getId();
        activityDto.name = activity.getName();
        activityDto.startDate = activity.getStartDate();
        activityDto.endDate = activity.getEndDate();
        activityDto.place = PlaceDto.fromPlace( activity.getPlace() );
        activityDto.activityUri = uriInfo.getBaseUriBuilder()
                                         .path( TripController.class )
                                         .path( TripController.class, "updateActivity" )
                                         .build( tripId, activity.getId() );

        return activityDto;
    }
}
