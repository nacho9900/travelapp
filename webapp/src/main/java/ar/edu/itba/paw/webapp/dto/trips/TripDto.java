package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.webapp.dto.validators.TripConstraint;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@TripConstraint
@JsonInclude( JsonInclude.Include.NON_NULL )
public class TripDto
{
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private String role;
    @NotNull
    @Future
    @JsonFormat( shape = JsonFormat.Shape.STRING,
                 pattern = "yyyy-MM-dd" )
    private Date startDate;
    @NotNull
    @Future
    @JsonFormat( shape = JsonFormat.Shape.STRING,
                 pattern = "yyyy-MM-dd" )
    private Date endDate;
    private TripJoinRequestDto userJoinRequest;

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

    public TripJoinRequestDto getUserJoinRequest() {
        return userJoinRequest;
    }

    public void setUserJoinRequest( TripJoinRequestDto userJoinRequest ) {
        this.userJoinRequest = userJoinRequest;
    }

    public String getRole() {
        return role;
    }

    public void setRole( String role ) {
        this.role = role;
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

    public Trip toTrip() {
        LocalDate startDate = this.startDate.toInstant().atZone( ZoneId.systemDefault() ).toLocalDate();

        LocalDate endDate = this.endDate.toInstant().atZone( ZoneId.systemDefault() ).toLocalDate();

        Trip trip;

        if ( this.id == null ) {
            trip = new Trip( 0, this.name, this.description, startDate, endDate );
        }
        else {
            trip = new Trip( this.id, 0, this.name, this.description, startDate, endDate );
        }

        return trip;
    }

    public static TripDto fromTrip( Trip trip ) {
        TripDto tripDto = new TripDto();
        tripDto.id = trip.getId();
        tripDto.name = trip.getName();
        tripDto.startDate = java.sql.Date.valueOf( trip.getStartDate() );
        tripDto.endDate = java.sql.Date.valueOf( trip.getEndDate() );
        tripDto.description = trip.getDescription();

        return tripDto;
    }
}
