package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.webapp.dto.serializers.CollectionSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ActivityListDto
{
    private final LocalDate from;
    private final LocalDate to;
    private String role;
    @JsonSerialize( using = CollectionSerializer.class )
    private final List<ActivityDto> activities;

    public ActivityListDto( List<ActivityDto> activities, LocalDate from, LocalDate to ) {
        this.from = from;
        this.to = to;
        this.activities = activities;
    }

    public List<ActivityDto> getActivities() {
        return activities;
    }

    public String getRole() {
        return role;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setRole( String role ) {
        this.role = role;
    }

    public static ActivityListDto fromActivityList( List<Activity> activities, LocalDate from, LocalDate to ) {
        List<ActivityDto> activityDtos = activities.stream().map( ActivityDto::fromActivity ).collect(
                Collectors.toList() );

        return new ActivityListDto( activityDtos, from, to );
    }
}
