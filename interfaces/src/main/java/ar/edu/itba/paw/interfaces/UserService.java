package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exception.EntityAlreadyExistsException;
import ar.edu.itba.paw.model.exception.InvalidTokenException;
import ar.edu.itba.paw.model.exception.InvalidUserException;
import ar.edu.itba.paw.model.exception.UserNotVerifiedException;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public interface UserService
{
    Optional<User> findById( final long id );

    Optional<User> findByUsername( final String email );

    User create( final String firstname, final String lastname, final String email, final String password, final LocalDate birthday, final String nationality, final String biography, Locale locale )
            throws EntityAlreadyExistsException;

    User update( User user, String firstname, String lastname, LocalDate birthday, String nationality,
                 String biography );

    boolean matchPassword( String passwordCurrentEncoded, String passwordNew );

    User changePassword( UUID tokenUUID, String passwordNew )
            throws InvalidTokenException, UserNotVerifiedException;

    User changePassword( User user, String password );

    boolean verifyEmail( UUID verificationToken );

    void initPasswordRecovery( String email, Locale locale ) throws InvalidUserException;
}
