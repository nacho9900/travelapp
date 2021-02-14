package ar.edu.itba.paw.webapp.dto.authentication;

import ar.edu.itba.paw.webapp.dto.users.UserDto;

import java.util.Date;

public class AuthDto
{
    private UserDto user;
    private String token;
    private Date expiresIn;

    public AuthDto( String token, Date expiresIn ) {
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

    public Date getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn( Date expiresIn ) {
        this.expiresIn = expiresIn;
    }
}
