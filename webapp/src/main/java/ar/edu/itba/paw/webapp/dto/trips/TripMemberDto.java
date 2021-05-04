package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.TripMemberRole;
import ar.edu.itba.paw.model.TripRate;
import ar.edu.itba.paw.webapp.controller.TripController;
import ar.edu.itba.paw.webapp.dto.serializers.CollectionSerializer;
import ar.edu.itba.paw.webapp.dto.users.UserDto;
import ar.edu.itba.paw.webapp.dto.validators.StringEnumConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.validator.constraints.NotBlank;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude( JsonInclude.Include.NON_NULL )
public class TripMemberDto
{
    private Long id;
    private UserDto user;
    private Boolean isActive;
    @NotBlank
    @StringEnumConstraint( enumClass = TripMemberRole.class )
    private String role;
    private URI memberUri;

    public TripMemberDto() {
        //For Jackson
    }

    public Long getId() {
        return id;
    }

    public UserDto getUser() {
        return user;
    }

    public Boolean getActive() {
        return isActive;
    }

    public String getRole() {
        return role;
    }

    public static TripMemberDto fromTripMember( TripMember tripMember, UriInfo uriInfo, long tripId ) {
        TripMemberDto tripMemberDto = new TripMemberDto();
        tripMemberDto.id = tripMember.getId();
        tripMemberDto.role = tripMember.getRole().name();
        tripMemberDto.isActive = tripMember.getActive();
        tripMemberDto.user = UserDto.fromUser( tripMember.getUser(), uriInfo );
        tripMemberDto.memberUri = uriInfo.getBaseUriBuilder()
                                         .path( TripController.class )
                                         .path( TripController.class, "updateMember" )
                                         .build( tripId, tripMember.getId() );


        return tripMemberDto;
    }
}
