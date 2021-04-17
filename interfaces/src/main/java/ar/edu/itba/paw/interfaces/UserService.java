package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.PasswordRecoveryToken;
import ar.edu.itba.paw.model.User;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserService
{
    Optional<User> findById( final long id );

    Optional<User> findByUsername( final String email );

    User create( final String firstname, final String lastname, final String email, final String password,
                 final LocalDate birthday, final String nationality, final String biography );

    User update( User user, String firstname, String lastname, LocalDate birthday, String nationality,
                 String biography );

    boolean matchPassword( String passwordCurrentEncoded, String passwordNew );

    User changePassword( User user, PasswordRecoveryToken token, String passwordNew );

    User changePassword( User user, String passwordNew );

    boolean verifyEmail( UUID verificationToken );
}
