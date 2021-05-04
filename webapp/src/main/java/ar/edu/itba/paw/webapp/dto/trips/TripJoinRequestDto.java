package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.TripJoinRequest;
import ar.edu.itba.paw.webapp.controller.TripController;
import ar.edu.itba.paw.webapp.dto.users.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;

@JsonInclude( JsonInclude.Include.NON_NULL )
public class TripJoinRequestDto
{
    private Long id;
    private LocalDateTime createdOn;
    private String message;
    private String status;
    private UserDto user;
    private URI acceptUri;
    private URI rejectUri;

    public TripJoinRequestDto() {
        //For Jackson
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public UserDto getUser() {
        return user;
    }

    public URI getAcceptUri() {
        return acceptUri;
    }

    public URI getRejectUri() {
        return rejectUri;
    }

    public static TripJoinRequestDto fromTripJoinRequest( TripJoinRequest tripJoinRequest, UriInfo uriInfo,
                                                          long tripId ) {
        TripJoinRequestDto joinRequestDto = new TripJoinRequestDto();
        joinRequestDto.id = tripJoinRequest.getId();
        joinRequestDto.createdOn = tripJoinRequest.getCreatedOn();
        joinRequestDto.message = tripJoinRequest.getMessage();
        joinRequestDto.status = tripJoinRequest.getStatus().name();
        joinRequestDto.acceptUri = uriInfo.getBaseUriBuilder()
                                          .path( TripController.class )
                                          .path( TripController.class, "acceptRequest" )
                                          .build( tripId, tripJoinRequest.getId() );
        joinRequestDto.rejectUri = uriInfo.getBaseUriBuilder()
                                          .path( TripController.class )
                                          .path( TripController.class, "rejectRequest" )
                                          .build( tripId, tripJoinRequest.getId() );

        if ( tripJoinRequest.getUser() != null ) {
            joinRequestDto.user = UserDto.fromUser( tripJoinRequest.getUser(), uriInfo );
        }

        return joinRequestDto;

    }
}
