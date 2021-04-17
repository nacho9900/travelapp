package ar.edu.itba.paw.webapp.exception;

import org.springframework.security.core.AuthenticationException;

public class UserAuthenticationException extends AuthenticationException
{
    public UserAuthenticationException( String msg ) {
        super( msg );
    }
}
