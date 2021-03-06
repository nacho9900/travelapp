package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.MailingService;
import ar.edu.itba.paw.interfaces.PasswordRecoveryTokenService;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.PasswordRecoveryToken;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exception.EntityAlreadyExistsException;
import ar.edu.itba.paw.model.exception.EntityNotFoundException;
import ar.edu.itba.paw.model.exception.InvalidTokenException;
import ar.edu.itba.paw.model.exception.InvalidUserException;
import ar.edu.itba.paw.model.exception.UserNotVerifiedException;
import ar.edu.itba.paw.model.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordRecoveryTokenService passwordRecoveryTokenService;

    @Autowired
    private MailingService mailingService;

    @Override
    public Optional<User> findById( long id ) {
        return userDao.findById( id );
    }

    @Override
    public Optional<User> findByUsername( String email ) {
        return userDao.findByUsername( email );
    }

    @Override
    public User create( String firstname, String lastname, String email, String password, LocalDate birthday,
                        String nationality, String biography, Locale locale )
            throws EntityAlreadyExistsException {
        if ( this.findByUsername( email ).isPresent() ) {
            throw new EntityAlreadyExistsException();
        }

        User user = userDao.create( firstname, lastname, email, passwordEncoder.encode( password ), birthday,
                                    nationality, biography, UUID.randomUUID() );

        mailingService.welcomeAndVerificationEmail( user.getFullName(), user.getEmail(),
                                                    user.getVerificationToken().toString(), locale );

        return user;
    }

    @Override
    public User update( User user, String firstname, String lastname, LocalDate birthday, String nationality,
                        String biography ) {
        user.setFirstname( firstname );
        user.setLastname( lastname );
        user.setBirthday( birthday );
        user.setNationality( nationality );
        user.setBiography( biography );

        return userDao.update( user );
    }

    @Override
    public boolean matchPassword( String passwordCurrentEncoded, String passwordNew ) {
        return passwordEncoder.matches( passwordNew, passwordCurrentEncoded );
    }

    @Override
    public User changePassword( UUID tokenUUID, String password )
            throws InvalidTokenException, UserNotVerifiedException {
        Optional<PasswordRecoveryToken> maybeToken = passwordRecoveryTokenService.findByToken( tokenUUID );

        if ( !maybeToken.isPresent() || !maybeToken.get().isValid() ) {
            throw new InvalidTokenException();
        }

        PasswordRecoveryToken token = maybeToken.get();
        User user = token.getUser();

        if ( !user.isVerified() ) {
            throw new UserNotVerifiedException();
        }

        passwordRecoveryTokenService.markAsUsed( token );
        return changePassword( user, password );
    }

    @Override
    public User changePassword( String username, String passwordCurrentRaw, String passwordNew )
            throws EntityNotFoundException, ValidationException {
        Optional<User> maybeUser = findByUsername( username );

        if ( !maybeUser.isPresent() ) {
            throw new EntityNotFoundException();
        }

        User user = maybeUser.get();

        if ( !matchPassword( user.getPassword(), passwordCurrentRaw ) ) {
            throw new ValidationException( "passwords didn't match" );
        }

        return changePassword( user, passwordNew );
    }

    private User changePassword( User user, String password ) {
        user.setPassword( passwordEncoder.encode( password ) );
        return userDao.update( user );
    }

    @Override
    public boolean verifyEmail( UUID verificationToken ) {
        Optional<User> maybeUser = userDao.findByVerificationToken( verificationToken );

        if ( !maybeUser.isPresent() ) {
            return false;
        }

        User user = maybeUser.get();
        user.setVerified( true );
        userDao.update( user );

        return true;
    }

    @Override
    public void initPasswordRecovery( String email, Locale locale ) throws InvalidUserException {
        Optional<User> maybeUser = this.findByUsername( email );

        if ( !maybeUser.isPresent() || !maybeUser.get().isVerified() ) {
            throw new InvalidUserException();
        }

        passwordRecoveryTokenService.createOrUpdate( maybeUser.get(), locale );
    }
}
