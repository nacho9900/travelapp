package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.TripMemberRole;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.interfaces.TripDao;
import ar.edu.itba.paw.interfaces.TripMemberService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.exception.CannotDeleteOwnerException;
import ar.edu.itba.paw.model.exception.EntityNotFoundException;
import ar.edu.itba.paw.model.exception.InvalidDateRangeException;
import ar.edu.itba.paw.model.exception.InvalidUserException;
import ar.edu.itba.paw.model.exception.UserNotMemberException;
import ar.edu.itba.paw.model.exception.UserNotOwnerOrAdminException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

@RunWith( MockitoJUnitRunner.class )
public class TestTripServiceImpl
{
    @InjectMocks
    private TripServiceImpl tripService;

    @Mock
    private UserService userServiceMock;

    @Mock
    private TripDao tripDaoMock;

    @Mock
    private TripMemberService tripMemberServiceMock;

    // USER DATA
    private final String USER_EMAIL = "nachonegrocaino@gmail.com";

    @Mock
    private User USER;

    //TRIP
    private final long ID = 1;
    private final String NAME = "Trip to Constantinople";
    private final String DESCRIPTION = "We are going to test Constantinople food and tour the markets.";
    private final LocalDate START_DATE = LocalDate.of( 2021, 6, 1 );
    private final LocalDate END_DATE = LocalDate.of( 2021, 6, 30 );

    private final Trip TRIP = new Trip( ID, NAME, DESCRIPTION, START_DATE, END_DATE );

    //OWNER
    @Mock
    private TripMember OWNER;

    @Mock
    private TripMember MEMBER;
    private final long MEMBER_ID = 2;

    @Test
    public void testCreate() throws InvalidUserException, InvalidDateRangeException {
        Mockito.when( USER.isVerified() ).thenReturn( true );

        Mockito.when( OWNER.getRole() ).thenReturn( TripMemberRole.OWNER );
        Mockito.when( OWNER.getUser() ).thenReturn( USER );

        Mockito.when( userServiceMock.findByUsername( Mockito.eq( USER_EMAIL ) ) ).thenReturn( Optional.of( USER ) );

        Mockito.when( tripDaoMock.create( Mockito.eq( NAME ), Mockito.eq( DESCRIPTION ), Mockito.eq( START_DATE ),
                                          Mockito.eq( END_DATE ) ) ).thenReturn( TRIP );

        Mockito.when( tripMemberServiceMock.createOwner( Mockito.eq( TRIP ), Mockito.eq( USER ) ) ).thenReturn( OWNER );

        Trip trip = tripService.create( USER_EMAIL, NAME, DESCRIPTION, START_DATE, END_DATE );

        Assert.assertNotNull( trip );
        Assert.assertNotNull( trip.getMembers() );
        Assert.assertFalse( trip.getMembers().isEmpty() );
        Assert.assertEquals( 1, trip.getMembers().size() );
        Assert.assertEquals( USER, trip.getMembers().get( 0 ).getUser() );
        Assert.assertEquals( TripMemberRole.OWNER, trip.getMembers().get( 0 ).getRole() );
    }

    @Test( expected = InvalidUserException.class )
    public void testCreateWhenUserIsNotVerified() throws InvalidUserException, InvalidDateRangeException {
        Mockito.when( USER.isVerified() ).thenReturn( false );

        Mockito.when( userServiceMock.findByUsername( Mockito.eq( USER_EMAIL ) ) ).thenReturn( Optional.of( USER ) );

        tripService.create( USER_EMAIL, NAME, DESCRIPTION, START_DATE, END_DATE );
    }

    @Test( expected = InvalidUserException.class )
    public void testCreateWhenThereIsNotUser() throws InvalidUserException, InvalidDateRangeException {
        Mockito.when( userServiceMock.findByUsername( Mockito.eq( USER_EMAIL ) ) ).thenReturn( Optional.empty() );

        tripService.create( USER_EMAIL, NAME, DESCRIPTION, START_DATE, END_DATE );
    }

    @Test( expected = InvalidDateRangeException.class )
    public void testCreateWhenDatesAreMissMatch() throws InvalidUserException, InvalidDateRangeException {
        Mockito.when( USER.isVerified() ).thenReturn( true );

        Mockito.when( userServiceMock.findByUsername( Mockito.eq( USER_EMAIL ) ) ).thenReturn( Optional.of( USER ) );

        tripService.create( USER_EMAIL, NAME, DESCRIPTION, END_DATE, START_DATE );
    }

    @Test( expected = UserNotOwnerOrAdminException.class )
    public void testUpdateWhenUserIsNotOwnerOrAdmin()
            throws EntityNotFoundException, UserNotOwnerOrAdminException, InvalidDateRangeException {
        Mockito.when( tripMemberServiceMock.isUserOwnerOrAdmin( Mockito.eq( ID ), Mockito.eq( USER_EMAIL ) ) )
               .thenReturn( false );

        Mockito.when( tripDaoMock.findById( Mockito.eq( ID ) ) ).thenReturn( Optional.of( TRIP ) );

        tripService.update( ID, NAME, DESCRIPTION, START_DATE, END_DATE, USER_EMAIL );
    }

    @Test( expected = EntityNotFoundException.class )
    public void testUpdateWhenTripNotExists()
            throws EntityNotFoundException, UserNotOwnerOrAdminException, InvalidDateRangeException {
        Mockito.when( tripDaoMock.findById( Mockito.eq( ID ) ) ).thenReturn( Optional.empty() );

        tripService.update( ID, NAME, DESCRIPTION, START_DATE, END_DATE, USER_EMAIL );
    }

    @Test( expected = InvalidDateRangeException.class )
    public void testUpdateWhenDatesAreMissMatch()
            throws EntityNotFoundException, UserNotOwnerOrAdminException, InvalidDateRangeException {
        Mockito.when( tripMemberServiceMock.isUserOwnerOrAdmin( Mockito.eq( ID ), Mockito.eq( USER_EMAIL ) ) )
               .thenReturn( true );

        Mockito.when( tripDaoMock.findById( Mockito.eq( ID ) ) ).thenReturn( Optional.of( TRIP ) );

        tripService.update( ID, NAME, DESCRIPTION, END_DATE, START_DATE, USER_EMAIL );
    }

    @Test
    public void testUpdate() throws EntityNotFoundException, UserNotOwnerOrAdminException, InvalidDateRangeException {
        Mockito.when( tripMemberServiceMock.isUserOwnerOrAdmin( Mockito.eq( ID ), Mockito.eq( USER_EMAIL ) ) )
               .thenReturn( true );

        Mockito.when( tripDaoMock.findById( Mockito.eq( ID ) ) ).thenReturn( Optional.of( TRIP ) );

        Mockito.when( tripDaoMock.update( Mockito.eq( TRIP ) ) ).thenReturn( TRIP );

        Trip trip = tripService.update( ID, NAME, DESCRIPTION, START_DATE, END_DATE, USER_EMAIL );

        Assert.assertNotNull( trip );
        Assert.assertEquals( TRIP, trip );
    }

    @Test
    public void testDeleteMember()
            throws CannotDeleteOwnerException, EntityNotFoundException, UserNotOwnerOrAdminException, UserNotMemberException {
        Mockito.when( tripDaoMock.findById( Mockito.eq( ID ) ) ).thenReturn( Optional.of( TRIP ) );

        Mockito.when( MEMBER.getRole() ).thenReturn( TripMemberRole.MEMBER );

        Mockito.when( tripMemberServiceMock.findById( Mockito.eq( MEMBER_ID ) ) ).thenReturn( Optional.of( MEMBER ) );

        Mockito.when( tripMemberServiceMock.isUserOwnerOrAdmin( Mockito.eq( ID ), Mockito.eq( USER_EMAIL ) ) )
               .thenReturn( true );

        tripService.deleteMember( ID, MEMBER_ID, USER_EMAIL, Locale.US );
    }

    @Test( expected = UserNotOwnerOrAdminException.class )
    public void testDeleteMemberWhenUserIsNotOwnerOrAdmin()
            throws CannotDeleteOwnerException, EntityNotFoundException, UserNotOwnerOrAdminException, UserNotMemberException {
        Mockito.when( tripDaoMock.findById( Mockito.eq( ID ) ) ).thenReturn( Optional.of( TRIP ) );

        Mockito.when( MEMBER.getRole() ).thenReturn( TripMemberRole.MEMBER );

        Mockito.when( tripMemberServiceMock.findById( Mockito.eq( MEMBER_ID ) ) ).thenReturn( Optional.of( MEMBER ) );

        Mockito.when( tripMemberServiceMock.isUserOwnerOrAdmin( Mockito.eq( ID ), Mockito.eq( USER_EMAIL ) ) )
               .thenReturn( false );

        tripService.deleteMember( ID, MEMBER_ID, USER_EMAIL, Locale.US );
    }

    @Test( expected = CannotDeleteOwnerException.class )
    public void testDeleteMemberWhenMemberIsOwner()
            throws CannotDeleteOwnerException, EntityNotFoundException, UserNotOwnerOrAdminException, UserNotMemberException {
        Mockito.when( tripDaoMock.findById( Mockito.eq( ID ) ) ).thenReturn( Optional.of( TRIP ) );

        Mockito.when( MEMBER.getRole() ).thenReturn( TripMemberRole.OWNER );

        Mockito.when( tripMemberServiceMock.findById( Mockito.eq( MEMBER_ID ) ) ).thenReturn( Optional.of( MEMBER ) );

        tripService.deleteMember( ID, MEMBER_ID, USER_EMAIL, Locale.US );
    }

    @Test( expected = EntityNotFoundException.class )
    public void testDeleteMemberWhenMemberNotExists()
            throws CannotDeleteOwnerException, EntityNotFoundException, UserNotOwnerOrAdminException, UserNotMemberException {
        Mockito.when( tripDaoMock.findById( Mockito.eq( ID ) ) ).thenReturn( Optional.of( TRIP ) );

        Mockito.when( tripMemberServiceMock.findById( Mockito.eq( MEMBER_ID ) ) ).thenReturn( Optional.empty() );

        tripService.deleteMember( ID, MEMBER_ID, USER_EMAIL, Locale.US );
    }

    @Test( expected = EntityNotFoundException.class )
    public void testDeleteMemberWhenTripNotExists()
            throws CannotDeleteOwnerException, EntityNotFoundException, UserNotOwnerOrAdminException, UserNotMemberException {
        Mockito.when( tripDaoMock.findById( Mockito.eq( ID ) ) ).thenReturn( Optional.empty() );

        tripService.deleteMember( ID, MEMBER_ID, USER_EMAIL, Locale.US );
    }
}
