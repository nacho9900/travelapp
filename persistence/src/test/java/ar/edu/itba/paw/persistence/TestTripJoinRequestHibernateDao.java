package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripDao;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.TripJoinRequest;
import ar.edu.itba.paw.model.TripJoinRequestStatus;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.Trip;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RunWith( SpringJUnit4ClassRunner.class )
@Sql( "classpath:schema.sql" )
@ContextConfiguration( classes = TestConfig.class )
@Transactional
public class TestTripJoinRequestHibernateDao
{
    @Autowired
    private TripJoinRequestHibernateDao joinRequestDao;

    @Autowired
    private TripDao tripDao;

    @Autowired
    private UserDao userDao;

    /* create */
    private final long TRIP_ID_CREATE = 2;
    private final long USER_ID_CREATE = 2;
    private Trip TRIP_CREATE;
    private User USER_CREATE;
    private final String MESSAGE_CREATE = "Que ganas de unirme";
    private final TripJoinRequestStatus STATUS_CREATE = TripJoinRequestStatus.PENDING;

    /* find */
    private final long TRIP_ID_FIND = 1;
    private final long USER_ID_FIND = 3;
    private Trip TRIP_FIND;
    private User USER_FIND;
    private final long ID_FIND = 1;
    private final String MESSAGE_FIND = "ME QUIERO UNIR";
    private final TripJoinRequestStatus STATUS_FIND = TripJoinRequestStatus.REJECTED;


    @Before
    public void before() {
        TRIP_FIND = tripDao.findById( TRIP_ID_FIND )
                           .get();
        TRIP_CREATE = tripDao.findById( TRIP_ID_FIND )
                             .get();
        USER_CREATE = userDao.findById( USER_ID_CREATE )
                             .get();
        USER_FIND = userDao.findById( USER_ID_FIND )
                           .get();
    }

    @Test
    public void testCreate() {
        final TripJoinRequest joinRequest = joinRequestDao.create( USER_CREATE, TRIP_CREATE, MESSAGE_CREATE );
        Assert.assertNotNull( joinRequest );
        Assert.assertEquals( USER_CREATE, joinRequest.getUser() );
        Assert.assertEquals( TRIP_CREATE, joinRequest.getTrip() );
        Assert.assertEquals( MESSAGE_CREATE, joinRequest.getMessage() );
        Assert.assertEquals( STATUS_CREATE, joinRequest.getStatus() );
    }

    @Test
    public void testFindById() {
        final Optional<TripJoinRequest> maybeJoinRequest = joinRequestDao.findById( ID_FIND );
        Assert.assertNotNull( maybeJoinRequest );
        Assert.assertTrue( maybeJoinRequest.isPresent() );
        final TripJoinRequest joinRequest = maybeJoinRequest.get();
        Assert.assertEquals( ID_FIND, joinRequest.getId() );
        Assert.assertEquals( USER_FIND, joinRequest.getUser() );
        Assert.assertEquals( TRIP_FIND, joinRequest.getTrip() );
        Assert.assertEquals( MESSAGE_FIND, joinRequest.getMessage() );
        Assert.assertEquals( STATUS_FIND, joinRequest.getStatus() );
    }

    @Test
    public void testFindByTripId() {
        final List<TripJoinRequest> joinRequests = joinRequestDao.getAllByTripId( TRIP_ID_FIND );
        Assert.assertNotNull( joinRequests );
        Assert.assertEquals( 1, joinRequests.size() );
        final TripJoinRequest joinRequest = joinRequests.get( 0 );
        Assert.assertEquals( ID_FIND, joinRequest.getId() );
        Assert.assertEquals( USER_FIND, joinRequest.getUser() );
        Assert.assertEquals( TRIP_FIND, joinRequest.getTrip() );
        Assert.assertEquals( MESSAGE_FIND, joinRequest.getMessage() );
        Assert.assertEquals( STATUS_FIND, joinRequest.getStatus() );
    }

    @Test
    public void testFindByTripIdAndStatus() {
        final List<TripJoinRequest> joinRequests = joinRequestDao.getAllByTripIdAndStatus( TRIP_ID_FIND, STATUS_FIND );
        Assert.assertNotNull( joinRequests );
        Assert.assertEquals( 1, joinRequests.size() );
        final TripJoinRequest joinRequest = joinRequests.get( 0 );
        Assert.assertEquals( ID_FIND, joinRequest.getId() );
        Assert.assertEquals( USER_FIND, joinRequest.getUser() );
        Assert.assertEquals( TRIP_FIND, joinRequest.getTrip() );
        Assert.assertEquals( MESSAGE_FIND, joinRequest.getMessage() );
        Assert.assertEquals( STATUS_FIND, joinRequest.getStatus() );
    }

    @Test
    public void testFindByTripIdAndStatusWhenNotExists() {
        final List<TripJoinRequest> joinRequests = joinRequestDao.getAllByTripIdAndStatus( TRIP_ID_FIND, STATUS_CREATE );
        Assert.assertNotNull( joinRequests );
        Assert.assertTrue( joinRequests.isEmpty() );
    }

    @Test
    public void testGetLastByTripIdAndUsername() {
        final Optional<TripJoinRequest> maybeJoinRequest = joinRequestDao.getLastByTripIdAndUsername( TRIP_ID_FIND,
                                                                                                      USER_FIND.getEmail() );
        Assert.assertNotNull( maybeJoinRequest );
        Assert.assertTrue( maybeJoinRequest.isPresent() );
        final TripJoinRequest joinRequest = maybeJoinRequest.get();
        Assert.assertEquals( ID_FIND, joinRequest.getId() );
        Assert.assertEquals( USER_FIND, joinRequest.getUser() );
        Assert.assertEquals( TRIP_FIND, joinRequest.getTrip() );
        Assert.assertEquals( MESSAGE_FIND, joinRequest.getMessage() );
        Assert.assertEquals( STATUS_FIND, joinRequest.getStatus() );
    }

    @Test
    public void testGetLastByTripIdAndUsernameWhenNotExists() {
        final Optional<TripJoinRequest> maybeJoinRequest = joinRequestDao.getLastByTripIdAndUsername( TRIP_ID_FIND,
                                                                                                      USER_CREATE.getEmail() );
        Assert.assertNotNull( maybeJoinRequest );
        Assert.assertFalse( maybeJoinRequest.isPresent() );
    }
}
