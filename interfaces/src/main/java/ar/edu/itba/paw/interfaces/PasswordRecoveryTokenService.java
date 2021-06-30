package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.PasswordRecoveryToken;
import ar.edu.itba.paw.model.User;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public interface PasswordRecoveryTokenService
{
    Optional<PasswordRecoveryToken> findByUserId( long userId );

    PasswordRecoveryToken createOrUpdate( User user, Locale locale );

    Optional<PasswordRecoveryToken> findByToken( UUID token );

    void markAsUsed( PasswordRecoveryToken token );
}
