package ar.edu.itba.paw.webapp.dto.trips;

import javax.validation.constraints.Size;

public class JoinTripDto
{
    @Size( max = 255 )
    private String message;

    public String getMessage() {
        return message;
    }
}
