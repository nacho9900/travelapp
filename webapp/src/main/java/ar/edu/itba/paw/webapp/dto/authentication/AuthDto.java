package ar.edu.itba.paw.webapp.dto.authentication;

import ar.edu.itba.paw.webapp.dto.users.UserDto;

import java.time.LocalDateTime;
import java.util.Date;

public class AuthDto
{
    private UserDto user;
    private String token;
    private LocalDateTime expiresIn;

    public AuthDto( String token, LocalDateTime expiresIn ) {
        this.token = token;
        this.expiresIn = expiresIn;
    }

    public AuthDto() {
        //For Jersey
    }

    public String getToken() {
        return token;
    }

    public void setToken( String token ) {
        this.token = token;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser( UserDto user ) {
        this.user = user;
    }

    public LocalDateTime getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn( LocalDateTime expiresIn ) {
        this.expiresIn = expiresIn;
    }
}
