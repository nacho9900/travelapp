package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.MailingService;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exception.EntityAlreadyExistsException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.Locale;
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

    @InjectMocks
    private final UserServiceImpl userService = new UserServiceImpl();

    @Mock
    private UserDao mockDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailingService mailingService;

    @Test
    public void testCreate() throws EntityAlreadyExistsException {
        Mockito.when( mockDao.create( Mockito.eq( FIRSTNAME ), Mockito.eq( LASTNAME ), Mockito.eq( EMAIL ),
                                      Mockito.eq( PASSWORD ), Mockito.eq( BIRTHDAY ), Mockito.eq( NATIONALITY ),
                                      Mockito.eq( BIOGRAPHY ), Mockito.any() ) )
               .thenReturn( new User( ID, FIRSTNAME, LASTNAME, EMAIL, PASSWORD, BIRTHDAY, NATIONALITY, BIOGRAPHY,
                                      VERIFICATION_TOKEN ) );

        Mockito.when( passwordEncoder.encode( Mockito.eq( PASSWORD ) ) ).thenReturn( PASSWORD );

        User user = userService.create( FIRSTNAME, LASTNAME, EMAIL, PASSWORD, BIRTHDAY, NATIONALITY, BIOGRAPHY,
                                        LOCALE );

        Assert.assertNotNull( user.getVerificationToken() );
        Assert.assertEquals( VERIFICATION_TOKEN, user.getVerificationToken() );
        Assert.assertFalse( user.isVerified() );
    }
}
