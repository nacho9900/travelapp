package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.TripMemberRole;
import ar.edu.itba.paw.model.TripRate;
import ar.edu.itba.paw.webapp.dto.serializers.CollectionSerializer;
import ar.edu.itba.paw.webapp.dto.users.UserDto;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

public class TripMemberDto
{
    private Long id;
    private UserDto userDto;
    private Boolean isActive;
    private String role;
    private RateDto rate;
    @JsonSerialize( using = CollectionSerializer.class )
    List<CommentDto> comments;

    public TripMemberDto() {
        //For Jackson
    }

    public Long getId() {
        return id;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public Boolean getActive() {
        return isActive;
    }

    public String getRole() {
        return role;
    }

    public RateDto getRate() {
        return rate;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public TripMember toTripMember() {
        return new TripMember( this.id, null, TripMemberRole.valueOf( this.role ), this.isActive,
                this.userDto != null ? this.userDto.toUser() : null, this.rate != null ? this.rate.toTripRate() : null,
                this.comments != null ? this.comments.stream()
                                                     .map( CommentDto::toTripComment )
                                                     .collect( Collectors.toList() ) : null );
    }

    public static TripMemberDto fromTripMember( TripMember tripMember, UriInfo uriInfo, boolean includeRate,
                                                boolean includeComments ) {
        TripMemberDto tripMemberDto = new TripMemberDto();
        tripMemberDto.id = tripMember.getId();
        tripMemberDto.role = tripMember.getRole()
                                       .name();
        tripMemberDto.isActive = tripMember.getActive();
        tripMemberDto.userDto = UserDto.fromUser( tripMember.getUser(), uriInfo );

        if ( includeRate ) {
            TripRate tripRate = tripMember.getRate();
            if ( tripRate != null ) {
                tripMemberDto.rate = RateDto.fromTripRate( tripRate, uriInfo, false );
            }
        }

        if ( includeComments ) {
            List<TripComment> tripComments = tripMember.getComments();
            if ( tripComments != null ) {
                tripMemberDto.comments = tripComments.stream()
                                                     .map( x -> CommentDto.fromComment( x, uriInfo, false ) )
                                                     .collect( Collectors.toList() );
            }
        }


        return tripMemberDto;
    }
}
