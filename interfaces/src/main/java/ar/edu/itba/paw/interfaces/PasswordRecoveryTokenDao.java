package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.PasswordRecoveryToken;
import ar.edu.itba.paw.model.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface PasswordRecoveryTokenDao
{
    Optional<PasswordRecoveryToken> findByUserId( long userId );

    PasswordRecoveryToken update( PasswordRecoveryToken token );

    PasswordRecoveryToken create( UUID token, LocalDateTime expiresIn, User user );

    Optional<PasswordRecoveryToken> findByToken( UUID token );
}
