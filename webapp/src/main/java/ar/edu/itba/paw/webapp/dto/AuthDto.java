package ar.edu.itba.paw.webapp.dto;

public class AuthDto
{

    private String token;
    private Long expiresIn;

    public AuthDto( String token, Long expiresIn ) {
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

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn( Long expiresIn ) {
        this.expiresIn = expiresIn;
    }
}
