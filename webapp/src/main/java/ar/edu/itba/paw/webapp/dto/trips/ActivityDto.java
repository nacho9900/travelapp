package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.Activity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@JsonInclude( JsonInclude.Include.NON_NULL )
public class ActivityDto
{
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    private String category;
    @NotNull
    @JsonFormat( shape = JsonFormat.Shape.STRING,
                 pattern = "yyyy-MM-dd" )
    private Date startDate;
    @NotNull
    @JsonFormat( shape = JsonFormat.Shape.STRING,
                 pattern = "yyyy-MM-dd" )
    private Date endDate;
    @NotNull
    private PlaceDto place;

    public ActivityDto() {
        //For Jackson
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public PlaceDto getPlace() {
        return this.place;
    }

    public Activity toActivity() {
        LocalDate startDate = this.startDate.toInstant()
                                            .atZone( ZoneId.systemDefault() )
                                            .toLocalDate();
        LocalDate endDate = this.endDate.toInstant()
                                        .atZone( ZoneId.systemDefault() )
                                        .toLocalDate();

        if(this.id != null) {
            return new Activity( this.id, this.name, this.place.toPlace(), null, startDate, endDate );
        } else {
            return new Activity( this.name, this.place.toPlace(), null, startDate, endDate );
        }
    }

    public static ActivityDto fromActivity( Activity activity ) {
        ActivityDto activityDto = new ActivityDto();
        activityDto.id = activity.getId();
        activityDto.name = activity.getName();
        activityDto.startDate = java.sql.Date.valueOf( activity.getStartDate() );
        activityDto.endDate = java.sql.Date.valueOf( activity.getEndDate() );
        activityDto.place = PlaceDto.fromPlace( activity.getPlace() );

        return activityDto;
    }
}
