package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.PasswordRecoveryToken;
import ar.edu.itba.paw.model.User;

public interface PasswordRecoveryTokenService
{
    PasswordRecoveryToken createOrUpdate( User user );
}
