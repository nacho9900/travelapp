package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.webapp.dto.serializers.CollectionSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

public class TripMemberListDto
{
    @JsonSerialize( using = CollectionSerializer.class )
    private List<TripMemberDto> members;

    public List<TripMemberDto> getMembers() {
        return members;
    }

    public static TripMemberListDto fromMembersList( List<TripMember> members, UriInfo uriInfo, long tripId ) {
        TripMemberListDto tripMemberListDto = new TripMemberListDto();
        tripMemberListDto.members = members.stream()
                                           .map( tripMember -> TripMemberDto.fromTripMember( tripMember, uriInfo,
                                                                                             tripId ) )
                                           .collect( Collectors.toList() );
        return tripMemberListDto;
    }
}
