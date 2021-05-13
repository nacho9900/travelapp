package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.webapp.dto.serializers.CollectionSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

public class ActivityListDto
{
    @JsonSerialize( using = CollectionSerializer.class )
    private final List<ActivityDto> activities;

    public ActivityListDto( List<ActivityDto> activities ) {
        this.activities = activities;
    }

    public List<ActivityDto> getActivities() {
        return activities;
    }

    public static ActivityListDto fromActivityList( List<Activity> activities, UriInfo uriInfo, long tripId ) {
        List<ActivityDto> activityDtos = activities.stream()
                                                   .map( x -> ActivityDto.fromActivity( x, uriInfo, tripId ) )
                                                   .collect( Collectors.toList() );

        return new ActivityListDto( activityDtos );
    }
}
