package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.Activity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ActivityDto
{
    private Long id;
    private String name;
    private String category;
    private Date startDate;
    private Date endDate;
    private PlaceDto place;

    public ActivityDto() {
        //For Jackson
    }

    public Long getId() {
        return id;
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

        return new Activity( this.id, this.name, this.category, this.place.toPlace(), null, startDate, endDate );
    }

    public static ActivityDto fromActivity( Activity activity ) {
        ActivityDto activityDto = new ActivityDto();
        activityDto.id = activity.getId();
        activityDto.name = activity.getName();
        activityDto.category = activity.getCategory();
        activityDto.startDate = java.sql.Date.valueOf( activity.getStartDate() );
        activityDto.endDate = java.sql.Date.valueOf( activity.getEndDate() );
        activityDto.place = PlaceDto.fromPlace( activity.getPlace() );

        return activityDto;
    }
}
