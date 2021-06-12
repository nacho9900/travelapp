package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripDao;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripMemberRole;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.TripMember;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith( SpringJUnit4ClassRunner.class )
@Sql( "classpath:schema.sql" )
@ContextConfiguration( classes = TestConfig.class )
@Transactional
public class TestTripMemberHibernateDao
{
    @Autowired
    private TripDao tripDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TripMemberHibernateDao tripMemberHibernateDao;

    /* trip */
    private final long TRIP_ID1 = 1;
    private final long TRIP_ID2 = 2;
    private Trip trip;

    /* user */
    private final long USER_ID1 = 1;
    private User user;

    /* create */
    private final TripMemberRole ROLE = TripMemberRole.OWNER;

    /* find */
    private final long ID_FIND = 1;


    @Before
    public void beforeEach() {
        trip = tripDao.findById( TRIP_ID1 )
                      .get();
        user = userDao.findById( USER_ID1 )
                      .get();
    }

    @Test
    public void testCreate() {
        final TripMember member = tripMemberHibernateDao.create( trip, user, ROLE, true );
        Assert.assertNotNull( member );
        Assert.assertTrue( member.getId() > 0 );
        Assert.assertTrue( member.getActive() );
        Assert.assertEquals( ROLE, member.getRole() );
    }

    @Test
    public void testMemberBelongsToATrip() {
        Assert.assertTrue( tripMemberHibernateDao.memberBelongsToTheTrip( ID_FIND, TRIP_ID1 ) );
        Assert.assertFalse( tripMemberHibernateDao.memberBelongsToTheTrip( ID_FIND, TRIP_ID2 ) );
    }

    @Test
    public void testFindAllAdmins() {

    }
}
