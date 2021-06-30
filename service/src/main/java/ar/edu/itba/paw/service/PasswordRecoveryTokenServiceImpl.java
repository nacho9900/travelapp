package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.MailingService;
import ar.edu.itba.paw.interfaces.PasswordRecoveryTokenDao;
import ar.edu.itba.paw.interfaces.PasswordRecoveryTokenService;
import ar.edu.itba.paw.model.PasswordRecoveryToken;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PasswordRecoveryTokenServiceImpl implements PasswordRecoveryTokenService
{
    @Autowired
    private PasswordRecoveryTokenDao passwordRecoveryTokenDao;

    @Autowired
    private MailingService mailingService;

    @Override
    public Optional<PasswordRecoveryToken> findByUserId( long userId ) {
        return passwordRecoveryTokenDao.findByUserId( userId );
    }

    @Override
    public PasswordRecoveryToken createOrUpdate( User user, Locale locale ) {
        Optional<PasswordRecoveryToken> maybeToken = findByUserId( user.getId() );
        PasswordRecoveryToken token;

        if ( maybeToken.isPresent() ) {
            PasswordRecoveryToken tokenToUpdate = maybeToken.get();
            tokenToUpdate.setToken( UUID.randomUUID() );
            tokenToUpdate.setExpiresIn( LocalDateTime.now( ZoneOffset.UTC ).plusHours( 3 ) );
            token = update( tokenToUpdate );
        }
        else {
            token = passwordRecoveryTokenDao.create( UUID.randomUUID(),
                                                     LocalDateTime.now( ZoneOffset.UTC ).plusHours( 3 ), user );
        }

        mailingService.sendPasswordRecoveryEmail( user.getFullName(), user.getEmail(), token.getToken().toString(),
                                                  locale );

        return token;
    }

    @Override
    public Optional<PasswordRecoveryToken> findByToken( UUID token ) {
        return passwordRecoveryTokenDao.findByToken( token );
    }

    @Override
    public void markAsUsed( PasswordRecoveryToken token ) {
        token.setUsed( true );
        update( token );
    }

    private PasswordRecoveryToken update( PasswordRecoveryToken token ) {
        return passwordRecoveryTokenDao.update( token );
    }
}
