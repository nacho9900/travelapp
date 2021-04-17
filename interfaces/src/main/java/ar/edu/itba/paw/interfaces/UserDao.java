package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.User;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDao
{
    Optional<User> findById( final long id );

    Optional<User> findByUsername( final String email );

    User create( final String firstname, final String lastname, final String email, final String password,
                 LocalDate birthday, final String nationality, final String biography, UUID verificationToken );

    User update( User u );

    Optional<User> findByVerificationToken( UUID verificationToken );
}
