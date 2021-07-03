package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.MailingService;
import ar.edu.itba.paw.interfaces.TripJoinRequestDao;
import ar.edu.itba.paw.interfaces.TripMemberService;
import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripJoinRequest;
import ar.edu.itba.paw.model.TripJoinRequestStatus;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exception.EntityNotFoundException;
import ar.edu.itba.paw.model.exception.UserAlreadyAMemberException;
import ar.edu.itba.paw.model.exception.UserAlreadyHaveAPendingRequestException;
import ar.edu.itba.paw.model.exception.UserNotOwnerOrAdminException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Optional;

@RunWith( MockitoJUnitRunner.class )
public class TestTripJoinRequestServiceImpl
{
    @InjectMocks
    private TripJoinRequestServiceImpl tripJoinRequestService;

    @Mock
    private TripJoinRequestDao tripJoinRequestDaoMock;

    @Mock
    private TripMemberService tripMemberServiceMock;

    @Mock
    private MailingService mailingServiceMock;

    @Mock
    private UserService userServiceMock;

    @Mock
    private TripService tripServiceMock;

    @Mock
    private User USER;

    @Mock
    private Trip TRIP;

    //CREATE
    private final long ID = 1;
    private final String USERNAME = "nachonegrotest@gmail.com";
    private final long TRIP_ID = 1;
    private final String MESSAGE = "JSAIDJAScc11391))()%%%!!!";
    private final Locale LOCALE = Locale.US;
    private final LocalDateTime CREATED_ON = LocalDateTime.of( 2021, 6, 30, 18, 0 );
    private final TripJoinRequestStatus STATUS_PENDING = TripJoinRequestStatus.PENDING;
    private TripJoinRequest TRIP_JOIN_REQUEST;
    @Mock
    private TripMember MEMBER;
    private final long MEMBER_ID = 1;

    @Test
    public void testCreate()
            throws EntityNotFoundException, UserAlreadyAMemberException, UserAlreadyHaveAPendingRequestException {
        TRIP_JOIN_REQUEST = new TripJoinRequest( ID, CREATED_ON, MESSAGE, STATUS_PENDING, TRIP, USER );

        Mockito.when( tripMemberServiceMock.isUserMember( Mockito.eq( TRIP_ID ), Mockito.eq( USERNAME ) ) )
               .thenReturn( false );

        Mockito.when( tripMemberServiceMock.getAllAdmins( Mockito.eq( TRIP_ID ) ) ).thenReturn( new LinkedList<>() );

        Mockito.when( userServiceMock.findByUsername( Mockito.eq( USERNAME ) ) ).thenReturn( Optional.of( USER ) );

        Mockito.when( tripServiceMock.findById( Mockito.eq( TRIP_ID ) ) ).thenReturn( Optional.of( TRIP ) );

        Mockito.when( tripJoinRequestDaoMock.create( Mockito.eq( USER ), Mockito.eq( TRIP ), Mockito.eq( MESSAGE ) ) )
               .thenReturn( TRIP_JOIN_REQUEST );

        Mockito.when(
                tripJoinRequestDaoMock.getLastByTripIdAndUsername( Mockito.eq( TRIP_ID ), Mockito.eq( USERNAME ) ) )
               .thenReturn( Optional.empty() );

        TripJoinRequest request = tripJoinRequestService.create( USERNAME, TRIP_ID, MESSAGE, LOCALE );

        Assert.assertNotNull( request );
        Assert.assertNotNull( request.getUser() );
        Assert.assertNotNull( request.getTrip() );
        Assert.assertEquals( USER, request.getUser() );
        Assert.assertEquals( TRIP, request.getTrip() );
        Assert.assertEquals( STATUS_PENDING, request.getStatus() );
        Assert.assertEquals( CREATED_ON, request.getCreatedOn() );
    }

    @Test( expected = UserAlreadyAMemberException.class )
    public void testCreateWhenUserIsMember()
            throws EntityNotFoundException, UserAlreadyAMemberException, UserAlreadyHaveAPendingRequestException {
        Mockito.when( tripMemberServiceMock.isUserMember( Mockito.eq( TRIP_ID ), Mockito.eq( USERNAME ) ) )
               .thenReturn( true );

        Mockito.when( userServiceMock.findByUsername( Mockito.eq( USERNAME ) ) ).thenReturn( Optional.of( USER ) );

        Mockito.when( tripServiceMock.findById( Mockito.eq( TRIP_ID ) ) ).thenReturn( Optional.of( TRIP ) );

        TripJoinRequest request = tripJoinRequestService.create( USERNAME, TRIP_ID, MESSAGE, LOCALE );
    }

    @Test( expected = EntityNotFoundException.class )
    public void testCreateWhenTripNotExists()
            throws EntityNotFoundException, UserAlreadyAMemberException, UserAlreadyHaveAPendingRequestException {
        Mockito.when( userServiceMock.findByUsername( Mockito.eq( USERNAME ) ) ).thenReturn( Optional.of( USER ) );

        Mockito.when( tripServiceMock.findById( Mockito.eq( TRIP_ID ) ) ).thenReturn( Optional.empty() );

        TripJoinRequest request = tripJoinRequestService.create( USERNAME, TRIP_ID, MESSAGE, LOCALE );
    }

    @Test( expected = UserAlreadyHaveAPendingRequestException.class )
    public void testCreateWhenUserAlreadyHavePendingRequest()
            throws EntityNotFoundException, UserAlreadyAMemberException, UserAlreadyHaveAPendingRequestException {
        TRIP_JOIN_REQUEST = new TripJoinRequest( ID, CREATED_ON, MESSAGE, STATUS_PENDING, TRIP, USER );

        Mockito.when( userServiceMock.findByUsername( Mockito.eq( USERNAME ) ) ).thenReturn( Optional.of( USER ) );

        Mockito.when( tripServiceMock.findById( Mockito.eq( TRIP_ID ) ) ).thenReturn( Optional.of( TRIP ) );

        Mockito.when(
                tripJoinRequestDaoMock.getLastByTripIdAndUsername( Mockito.eq( TRIP_ID ), Mockito.eq( USERNAME ) ) )
               .thenReturn( Optional.of( TRIP_JOIN_REQUEST ) );

        TripJoinRequest request = tripJoinRequestService.create( USERNAME, TRIP_ID, MESSAGE, LOCALE );
    }

    @Test
    public void testAccept() throws EntityNotFoundException, UserNotOwnerOrAdminException {
        TRIP_JOIN_REQUEST = new TripJoinRequest( ID, CREATED_ON, MESSAGE, STATUS_PENDING, TRIP, USER );
        TripJoinRequest acceptedRequest = new TripJoinRequest( ID, CREATED_ON, MESSAGE, TripJoinRequestStatus.ACCEPTED,
                                                               TRIP, USER );

        Mockito.when( tripMemberServiceMock.isUserOwnerOrAdmin( Mockito.eq( TRIP_ID ), Mockito.eq( USERNAME ) ) )
               .thenReturn( true );

        Mockito.when( tripJoinRequestDaoMock.findById( Mockito.eq( ID ) ) )
               .thenReturn( Optional.of( TRIP_JOIN_REQUEST ) );

        Mockito.when( tripMemberServiceMock.create( Mockito.eq( TRIP ), Mockito.eq( USER ) ) ).thenReturn( MEMBER );

        Mockito.when( TRIP.getId() ).thenReturn( TRIP_ID );

        Mockito.when( tripJoinRequestDaoMock.update( Mockito.eq( TRIP_JOIN_REQUEST ) ) ).thenReturn( acceptedRequest );

        TripMember member = tripJoinRequestService.accept( ID, USERNAME, LOCALE );

        Assert.assertNotNull( member );
    }

    @Test( expected = UserNotOwnerOrAdminException.class )
    public void testAcceptWhenUserNotAdminOrOwner() throws EntityNotFoundException, UserNotOwnerOrAdminException {
        TRIP_JOIN_REQUEST = new TripJoinRequest( ID, CREATED_ON, MESSAGE, STATUS_PENDING, TRIP, USER );

        Mockito.when( tripJoinRequestDaoMock.findById( Mockito.eq( ID ) ) )
               .thenReturn( Optional.of( TRIP_JOIN_REQUEST ) );

        tripJoinRequestService.accept( ID, USERNAME, LOCALE );
    }

    @Test( expected = EntityNotFoundException.class )
    public void testAcceptWhenRequestDoestExists() throws EntityNotFoundException, UserNotOwnerOrAdminException {
        TRIP_JOIN_REQUEST = new TripJoinRequest( ID, CREATED_ON, MESSAGE, STATUS_PENDING, TRIP, USER );

        Mockito.when( tripJoinRequestDaoMock.findById( Mockito.eq( ID ) ) ).thenReturn( Optional.empty() );

        tripJoinRequestService.accept( ID, USERNAME, LOCALE );
    }

    @Test
    public void testReject() throws EntityNotFoundException, UserNotOwnerOrAdminException {
        TRIP_JOIN_REQUEST = new TripJoinRequest( ID, CREATED_ON, MESSAGE, STATUS_PENDING, TRIP, USER );
        TripJoinRequest rejectedRequest = new TripJoinRequest( ID, CREATED_ON, MESSAGE, TripJoinRequestStatus.REJECTED,
                                                               TRIP, USER );
        Mockito.when( tripMemberServiceMock.isUserOwnerOrAdmin( Mockito.eq( TRIP_ID ), Mockito.eq( USERNAME ) ) )
               .thenReturn( true );

        Mockito.when( tripJoinRequestDaoMock.findById( Mockito.eq( ID ) ) )
               .thenReturn( Optional.of( TRIP_JOIN_REQUEST ) );

        Mockito.when( tripJoinRequestDaoMock.update( Mockito.eq( TRIP_JOIN_REQUEST ) ) ).thenReturn( rejectedRequest );

        Mockito.when( TRIP.getId() ).thenReturn( TRIP_ID );

        TripJoinRequest request = tripJoinRequestService.reject( ID, USERNAME );

        Assert.assertNotNull( request );
        Assert.assertEquals( TripJoinRequestStatus.REJECTED, request.getStatus() );
    }
}
