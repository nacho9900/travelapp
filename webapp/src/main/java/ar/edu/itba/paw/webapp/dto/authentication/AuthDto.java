package ar.edu.itba.paw.webapp.dto.authentication;

import java.util.Date;

public class AuthDto
{

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

    public Date getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn( Date expiresIn ) {
        this.expiresIn = expiresIn;
    }
}
