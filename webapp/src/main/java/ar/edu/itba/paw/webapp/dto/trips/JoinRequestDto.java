package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.TripJoinRequest;
import ar.edu.itba.paw.model.TripJoinRequestStatus;
import ar.edu.itba.paw.webapp.dto.users.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.ws.rs.core.UriInfo;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JoinRequestDto
{
    private Long id;
    @JsonFormat( shape = JsonFormat.Shape.STRING,
                 pattern = "yyyy-MM-dd hh:MM:ss" )
    private Date createdOn;
    private String message;
    private String status;
    private UserDto user;

    public JoinRequestDto() {
        //For Jackson
    }

    public Long getId() {
        return id;
    }

    public Date getCreatedOn() {
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

    public TripJoinRequest toTripJoinRequest() {
        LocalDateTime createdOn = this.createdOn.toInstant()
                                                .atZone( ZoneId.systemDefault() )
                                                .toLocalDateTime();

        return new TripJoinRequest( this.id, createdOn, this.message, TripJoinRequestStatus.valueOf( this.status ),
                null, this.user.toUser() );
    }

    public static JoinRequestDto fromTripJoinRequest( TripJoinRequest tripJoinRequest ) {
        JoinRequestDto joinRequestDto = new JoinRequestDto();
        joinRequestDto.id = tripJoinRequest.getId();
        joinRequestDto.createdOn = java.sql.Timestamp.valueOf( tripJoinRequest.getCreatedOn() );
        joinRequestDto.message = tripJoinRequest.getMessage();
        joinRequestDto.status = tripJoinRequest.getStatus()
                                               .name();
        joinRequestDto.user = UserDto.fromUser( tripJoinRequest.getUser() );

        return joinRequestDto;

    }
}
