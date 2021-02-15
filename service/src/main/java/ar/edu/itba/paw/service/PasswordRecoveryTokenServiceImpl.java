package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.PasswordRecoveryTokenDao;
import ar.edu.itba.paw.interfaces.PasswordRecoveryTokenService;
import ar.edu.itba.paw.model.PasswordRecoveryToken;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PasswordRecoveryTokenServiceImpl implements PasswordRecoveryTokenService
{
    @Autowired
    private PasswordRecoveryTokenDao passwordRecoveryTokenDao;

    public Optional<PasswordRecoveryToken> findByUserId( long userId ) {
        return passwordRecoveryTokenDao.findByUserId( userId );
    }

    @Override
    public PasswordRecoveryToken createOrUpdate( User user ) {
        Optional<PasswordRecoveryToken> maybeToken = findByUserId( user.getId() );

        if ( maybeToken.isPresent() ) {
            PasswordRecoveryToken token = maybeToken.get();
            token.setToken( UUID.randomUUID() );
            token.setExpiresIn( LocalDateTime.now().plusHours( 3 ) );
            return update( token );
        }
        else {
            return passwordRecoveryTokenDao.create( UUID.randomUUID(), LocalDateTime.now().plusHours( 3 ), user );
        }
    }

    private PasswordRecoveryToken update( PasswordRecoveryToken token ) {
        return passwordRecoveryTokenDao.update( token );
    }
}
