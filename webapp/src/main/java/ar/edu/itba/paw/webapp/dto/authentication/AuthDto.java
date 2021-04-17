package ar.edu.itba.paw.webapp.dto.authentication;

import java.time.LocalDateTime;

public class AuthDto
{
    private String token;
    private LocalDateTime expiresIn;
    private long userId;

    public AuthDto( String token, LocalDateTime expiresIn, long userId ) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.userId = userId;
    }

    public AuthDto() {
        //For Jersey
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getExpiresIn() {
        return expiresIn;
    }

    public long getUserId() {
        return userId;
    }
}
