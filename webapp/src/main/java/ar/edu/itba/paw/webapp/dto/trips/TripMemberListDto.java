package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.webapp.dto.serializers.CollectionSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;
import java.util.stream.Collectors;

public class TripMemberListDto
{
    private String role;

    @JsonSerialize( using = CollectionSerializer.class )
    private List<TripMemberDto> members;

    public List<TripMemberDto> getMembers() {
        return members;
    }

    public String getRole() {
        return role;
    }

    public void setRole( String role ) {
        this.role = role;
    }

    public static TripMemberListDto fromMembersList( List<TripMember> members ) {
        TripMemberListDto tripMemberListDto = new TripMemberListDto();
        tripMemberListDto.members = members.stream().map(
                tripMember -> TripMemberDto.fromTripMember( tripMember, true, false ) ).collect( Collectors.toList() );
        return tripMemberListDto;
    }
}
