package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.MailingService;
import ar.edu.itba.paw.interfaces.PasswordRecoveryTokenService;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.PasswordRecoveryToken;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exception.EntityAlreadyExistsException;
import ar.edu.itba.paw.model.exception.EntityNotFoundException;
import ar.edu.itba.paw.model.exception.InvalidTokenException;
import ar.edu.itba.paw.model.exception.UserNotVerifiedException;
import ar.edu.itba.paw.model.exception.ValidationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

@RunWith( MockitoJUnitRunner.class )
public class UserServiceImplTest
{
    private final int ID = 1;
    private final String FIRSTNAME = "Carló";
    private final String LASTNAME = "Magno";
    private final String FULLNAME = FIRSTNAME + " " + LASTNAME;
    private final String EMAIL = "nachonegrocaino@gmail.com";
    private final String PASSWORD = "carlomagno68!#";
    private final LocalDate BIRTHDAY = LocalDate.of( 768, 4, 2 );
    private final String NATIONALITY = "AR";
    private final String BIOGRAPHY = "bio";
    private final UUID VERIFICATION_TOKEN = UUID.fromString( "b60cd648-74a3-11eb-9439-0242ac130002" );
    private final Locale LOCALE = Locale.ENGLISH;

    // Update
    private final String PASSWORD_NEW = "a==!!==a123";

    // ChangePassword
    private final UUID RECOVERY_TOKEN = UUID.fromString( "b60cd648-74a3-11eb-9439-0242ac130002" );
    private final LocalDateTime RECOVERY_EXPIRATION_NOT_EXPIRED = LocalDateTime.now( ZoneOffset.UTC ).plusHours( 3 );
    private final LocalDateTime RECOVERY_EXPIRATION_EXPIRED = LocalDateTime.now( ZoneOffset.UTC ).minusHours( 5 );

    @InjectMocks
    private final UserServiceImpl userService = new UserServiceImpl();

    @Mock
    private UserDao userDaoMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @Mock
    private MailingService mailingServiceMock;

    @Mock
    private PasswordRecoveryTokenService passwordRecoveryTokenServiceMock;

    @Test
    public void testCreate() throws EntityAlreadyExistsException {
        Mockito.when( userDaoMock.create( Mockito.eq( FIRSTNAME ), Mockito.eq( LASTNAME ), Mockito.eq( EMAIL ),
                                          Mockito.eq( PASSWORD ), Mockito.eq( BIRTHDAY ), Mockito.eq( NATIONALITY ),
                                          Mockito.eq( BIOGRAPHY ), Mockito.any() ) )
               .thenReturn( new User( ID, FIRSTNAME, LASTNAME, EMAIL, PASSWORD, BIRTHDAY, NATIONALITY, BIOGRAPHY,
                                      VERIFICATION_TOKEN ) );

        Mockito.when( passwordEncoderMock.encode( Mockito.eq( PASSWORD ) ) ).thenReturn( PASSWORD );

        User user = userService.create( FIRSTNAME, LASTNAME, EMAIL, PASSWORD, BIRTHDAY, NATIONALITY, BIOGRAPHY,
                                        LOCALE );

        Assert.assertNotNull( user.getVerificationToken() );
        Assert.assertEquals( VERIFICATION_TOKEN, user.getVerificationToken() );
        Assert.assertFalse( user.isVerified() );
    }

    @Test( expected = EntityAlreadyExistsException.class )
    public void testCreateWhenUserAlreadyExists() throws EntityAlreadyExistsException {
        Mockito.when( userDaoMock.findByUsername( EMAIL ) )
               .thenReturn( Optional.of(
                       new User( ID, FIRSTNAME, LASTNAME, EMAIL, PASSWORD, BIRTHDAY, NATIONALITY, BIOGRAPHY,
                                 VERIFICATION_TOKEN ) ) );

        User user = userService.create( FIRSTNAME, LASTNAME, EMAIL, PASSWORD, BIRTHDAY, NATIONALITY, BIOGRAPHY,
                                        LOCALE );
    }

    @Test
    public void testFindByIdWhenUserExists() {
        Mockito.when( userDaoMock.findById( ID ) )
               .thenReturn( Optional.of(
                       new User( ID, FIRSTNAME, LASTNAME, EMAIL, PASSWORD, BIRTHDAY, NATIONALITY, BIOGRAPHY,
                                 VERIFICATION_TOKEN ) ) );

        Optional<User> maybeUser = userService.findById( ID );

        Assert.assertNotNull( maybeUser );
        Assert.assertTrue( maybeUser.isPresent() );

        User user = maybeUser.get();
        Assert.assertEquals( ID, user.getId() );
    }

    @Test
    public void testFindByUsernameWhenUserExists() {
        Mockito.when( userDaoMock.findByUsername( EMAIL ) )
               .thenReturn( Optional.of(
                       new User( ID, FIRSTNAME, LASTNAME, EMAIL, PASSWORD, BIRTHDAY, NATIONALITY, BIOGRAPHY,
                                 VERIFICATION_TOKEN ) ) );

        Optional<User> maybeUser = userService.findByUsername( EMAIL );

        Assert.assertNotNull( maybeUser );
        Assert.assertTrue( maybeUser.isPresent() );

        User user = maybeUser.get();
        Assert.assertEquals( EMAIL, user.getEmail() );
    }

    @Test
    public void testFindByIdWhenUserDoestExists() {
        Mockito.when( userDaoMock.findById( ID ) ).thenReturn( Optional.empty() );

        Optional<User> maybeUser = userService.findById( ID );

        Assert.assertFalse( maybeUser.isPresent() );
    }

    @Test
    public void testFindByUsernameWhenUserDoestExists() {
        Mockito.when( userDaoMock.findByUsername( EMAIL ) ).thenReturn( Optional.empty() );

        Optional<User> maybeUser = userService.findByUsername( EMAIL );

        Assert.assertFalse( maybeUser.isPresent() );
    }

    @Test
    public void testChangePasswordWhenUserExists() throws ValidationException, EntityNotFoundException {
        Mockito.when( userDaoMock.findByUsername( EMAIL ) )
               .thenReturn( Optional.of(
                       new User( ID, FIRSTNAME, LASTNAME, EMAIL, PASSWORD, BIRTHDAY, NATIONALITY, BIOGRAPHY,
                                 VERIFICATION_TOKEN ) ) );

        Mockito.when( passwordEncoderMock.matches( Mockito.eq( PASSWORD ), Mockito.eq( PASSWORD ) ) )
               .thenReturn( true );

        Mockito.when( userDaoMock.update( Mockito.any() ) )
               .thenReturn( new User( ID, FIRSTNAME, LASTNAME, EMAIL, PASSWORD_NEW, BIRTHDAY, NATIONALITY, BIOGRAPHY,
                                      VERIFICATION_TOKEN ) );

        User user = userService.changePassword( EMAIL, PASSWORD, PASSWORD_NEW );

        Assert.assertEquals( PASSWORD_NEW, user.getPassword() );
    }

    @Test
    public void testChangePasswordWithTokenWhenUserExists() throws InvalidTokenException, UserNotVerifiedException {
        Mockito.when( userDaoMock.update( Mockito.any() ) )
               .thenReturn( new User( ID, FIRSTNAME, LASTNAME, EMAIL, PASSWORD_NEW, BIRTHDAY, NATIONALITY, BIOGRAPHY,
                                      VERIFICATION_TOKEN, true ) );

        Mockito.when( passwordRecoveryTokenServiceMock.findByToken( Mockito.eq( RECOVERY_TOKEN ) ) )
               .thenReturn( Optional.of( new PasswordRecoveryToken( RECOVERY_TOKEN, RECOVERY_EXPIRATION_NOT_EXPIRED,
                                                                    new User( ID, FIRSTNAME, LASTNAME, EMAIL, PASSWORD,
                                                                              BIRTHDAY, NATIONALITY, BIOGRAPHY,
                                                                              VERIFICATION_TOKEN, true ) ) ) );

        User user = userService.changePassword( RECOVERY_TOKEN, PASSWORD_NEW );

        Assert.assertEquals( PASSWORD_NEW, user.getPassword() );
    }

    @Test( expected = InvalidTokenException.class )
    public void testChangePasswordWithInvalidToken() throws InvalidTokenException, UserNotVerifiedException {
        Mockito.when( passwordRecoveryTokenServiceMock.findByToken( Mockito.eq( RECOVERY_TOKEN ) ) )
               .thenReturn( Optional.of( new PasswordRecoveryToken( RECOVERY_TOKEN, RECOVERY_EXPIRATION_EXPIRED,
                                                                    new User( ID, FIRSTNAME, LASTNAME, EMAIL, PASSWORD,
                                                                              BIRTHDAY, NATIONALITY, BIOGRAPHY,
                                                                              VERIFICATION_TOKEN, true ) ) ) );

        User user = userService.changePassword( RECOVERY_TOKEN, PASSWORD_NEW );
    }

    @Test( expected = UserNotVerifiedException.class )
    public void testChangePasswordWithTokenAndUserNotVerified() throws InvalidTokenException, UserNotVerifiedException {
        Mockito.when( passwordRecoveryTokenServiceMock.findByToken( Mockito.eq( RECOVERY_TOKEN ) ) )
               .thenReturn( Optional.of( new PasswordRecoveryToken( RECOVERY_TOKEN, RECOVERY_EXPIRATION_NOT_EXPIRED,
                                                                    new User( ID, FIRSTNAME, LASTNAME, EMAIL, PASSWORD,
                                                                              BIRTHDAY, NATIONALITY, BIOGRAPHY,
                                                                              VERIFICATION_TOKEN, false ) ) ) );

        User user = userService.changePassword( RECOVERY_TOKEN, PASSWORD_NEW );
    }

    @Test
    public void testVerifyEmail() {
        Mockito.when( userDaoMock.findByVerificationToken( Mockito.eq( VERIFICATION_TOKEN ) ) )
               .thenReturn( Optional.of(
                       new User( ID, FIRSTNAME, LASTNAME, EMAIL, PASSWORD, BIRTHDAY, NATIONALITY, BIOGRAPHY,
                                 VERIFICATION_TOKEN ) ) );

        Assert.assertTrue( userService.verifyEmail( VERIFICATION_TOKEN ) );
    }

    @Test
    public void testVerifyEmailWhenInvalidToken() {
        Mockito.when( userDaoMock.findByVerificationToken( Mockito.eq( VERIFICATION_TOKEN ) ) )
               .thenReturn( Optional.empty() );

        Assert.assertFalse( userService.verifyEmail( VERIFICATION_TOKEN ) );
    }
}
