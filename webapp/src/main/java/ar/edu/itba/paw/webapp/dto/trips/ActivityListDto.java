package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.webapp.dto.serializers.CollectionSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;
import java.util.stream.Collectors;

public class ActivityListDto
{
    private String role;

    @JsonSerialize( using = CollectionSerializer.class )
    private List<ActivityDto> activities;

    public List<ActivityDto> getActivities() {
        return activities;
    }

    public String getRole() {
        return role;
    }

    public void setRole( String role ) {
        this.role = role;
    }

    public static ActivityListDto fromActivityList( List<Activity> activities ) {
        ActivityListDto activityListDto = new ActivityListDto();
        activityListDto.activities = activities.stream().map( ActivityDto::fromActivity ).collect(
                Collectors.toList() );
        return activityListDto;
    }
}
