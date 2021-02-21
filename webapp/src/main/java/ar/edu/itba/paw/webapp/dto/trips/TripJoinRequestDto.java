package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.TripJoinRequest;
import ar.edu.itba.paw.webapp.dto.users.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Date;

@JsonInclude( JsonInclude.Include.NON_NULL )
public class TripJoinRequestDto
{
    private Long id;
    @JsonFormat( shape = JsonFormat.Shape.STRING,
                 pattern = "yyyy-MM-dd HH:mm:ss" )
    private LocalDateTime createdOn;
    private String message;
    private String status;
    private UserDto user;

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

    public static TripJoinRequestDto fromTripJoinRequest( TripJoinRequest tripJoinRequest, boolean includeUser ) {
        TripJoinRequestDto joinRequestDto = new TripJoinRequestDto();
        joinRequestDto.id = tripJoinRequest.getId();
        joinRequestDto.createdOn = tripJoinRequest.getCreatedOn();
        joinRequestDto.message = tripJoinRequest.getMessage();
        joinRequestDto.status = tripJoinRequest.getStatus().name();

        if ( tripJoinRequest.getUser() != null && includeUser ) {
            joinRequestDto.user = UserDto.fromUser( tripJoinRequest.getUser() );
        }

        return joinRequestDto;

    }
}
