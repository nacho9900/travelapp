package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
import com.sun.xml.internal.bind.v2.model.core.ID;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@RunWith( SpringJUnit4ClassRunner.class )
@Sql( "classpath:schema.sql" )
@ContextConfiguration( classes = TestConfig.class )
@Transactional
public class TestUserHibernateDao
{
    @Autowired
    private UserHibernateDao userDao;

    /* Test Create */
    private final String FIRSTNAME = "Carló";
    private final String LASTNAME = "Magno";
    private final String EMAIL = "carlomagno@gmail.com";
    private final String PASSWORD = "carlomagno68!#";
    private final LocalDate BIRTHDAY = LocalDate.of( 768, 4, 2 );
    private final String NATIONALITY = "AR";
    private final UUID VERIFICATION_TOKEN = UUID.fromString( "b60cd648-74a3-11eb-9439-0242ac130002" );

    /* Test Find */
    private final long ID_FIND = 1;
    private final String EMAIL_FIND = "inegro@itba.edu.ar";

    /* Test Update */
    private final String NEW_FIRSTNAME = "Ignacio Nicolás";
    private final String PREV_FIRSTNAME = "Ignacio";
    private final String PREV_LASTNAME = "Negro Caino";
    private final String PREV_EMAIL = "inegro@itba.edu.ar";
    private final String PREV_PASSWORD = "v3r1s3cr3t3p4ss";
    private final LocalDate PREV_BIRTHDAY = LocalDate.of( 1995, 11, 30 );
    private final String PREV_NATIONALITY = "AR";
    private final UUID PREV_VERIFICATION_TOKEN = UUID.fromString( "b60cd648-74a3-11eb-9439-0242ac130002" );

    @Test
    public void testCreate() {
        final User user = userDao.create( FIRSTNAME, LASTNAME, EMAIL, PASSWORD, BIRTHDAY, NATIONALITY, "",
                                          VERIFICATION_TOKEN );
        Assert.assertNotNull( user );
        Assert.assertTrue( user.getId() > 0 );
        Assert.assertEquals( FIRSTNAME, user.getFirstname() );
        Assert.assertEquals( LASTNAME, user.getLastname() );
        Assert.assertEquals( EMAIL, user.getEmail() );
        Assert.assertEquals( PASSWORD, user.getPassword() );
        Assert.assertEquals( BIRTHDAY, user.getBirthday() );
        Assert.assertEquals( NATIONALITY, user.getNationality() );
        Assert.assertEquals( VERIFICATION_TOKEN, user.getVerificationToken() );
    }

    @Test
    public void testFindByUsername() {
        final Optional<User> maybeUser = userDao.findByUsername( EMAIL_FIND );
        Assert.assertTrue( maybeUser.isPresent() );
        Assert.assertEquals( EMAIL_FIND, maybeUser.get()
                                                  .getEmail() );
    }

    @Test
    public void testFindById() {
        final Optional<User> maybeUser = userDao.findById( ID_FIND );
        Assert.assertTrue( maybeUser.isPresent() );
        Assert.assertEquals( ID_FIND, maybeUser.get()
                                               .getId() );
    }

    @Test
    public void testFindByVerificationToken() {
        final Optional<User> maybeUser = userDao.findByVerificationToken( VERIFICATION_TOKEN );
        Assert.assertTrue( maybeUser.isPresent() );
        Assert.assertEquals( maybeUser.get().getVerificationToken(), VERIFICATION_TOKEN );
    }

    @Test
    public void testUpdate() {
        final Optional<User> maybeUser = userDao.findById( ID_FIND );
        Assert.assertTrue( maybeUser.isPresent() );
        User user = maybeUser.get();
        user.setFirstname( NEW_FIRSTNAME );
        user = userDao.update( user );
        Assert.assertEquals( ID_FIND, user.getId() );
        Assert.assertEquals( NEW_FIRSTNAME, user.getFirstname() );
        Assert.assertEquals( PREV_LASTNAME, user.getLastname() );
        Assert.assertEquals( PREV_EMAIL, user.getEmail() );
        Assert.assertEquals( PREV_PASSWORD, user.getPassword() );
        Assert.assertEquals( PREV_BIRTHDAY, user.getBirthday() );
        Assert.assertEquals( PREV_NATIONALITY, user.getNationality() );
        Assert.assertEquals( PREV_VERIFICATION_TOKEN, user.getVerificationToken() );
    }
}
