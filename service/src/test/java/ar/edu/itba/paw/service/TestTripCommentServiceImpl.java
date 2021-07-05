package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.TripCommentsDao;
import ar.edu.itba.paw.interfaces.TripMemberService;
import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.exception.UserNotMemberException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith( MockitoJUnitRunner.class )
public class TestTripCommentServiceImpl
{
    @InjectMocks
    private TripCommentServiceImpl tripCommentService;

    @Mock
    private TripCommentsDao tripCommentsDaoMock;

    @Mock
    private TripMemberService tripMemberServiceMock;

    @Mock
    private TripMember MEMBER;

    //COMMENT
    private final long ID = 755;
    private final LocalDateTime CREATED_ON = LocalDateTime.now();
    private final String COMMENT = "This is a test comment, have fun!";
    private final TripComment TRIP_COMMENT = new TripComment( ID, MEMBER, COMMENT, CREATED_ON );

    //TRIP
    private final long TRIP_ID = 8000;

    //USER
    private final String USERNAME = "lalala@lalala.com";

    @Test
    public void testCreate() throws UserNotMemberException {
        Mockito.when( tripMemberServiceMock.findByTripIdAndUsername( Mockito.eq( TRIP_ID ), Mockito.eq( USERNAME ),
                                                                     Mockito.eq( USERNAME ) ) )
               .thenReturn( Optional.of( MEMBER ) );

        Mockito.when( tripCommentsDaoMock.create( Mockito.eq( MEMBER ), Mockito.eq( COMMENT ) ) )
               .thenReturn( TRIP_COMMENT );

        TripComment comment = tripCommentService.create( TRIP_ID, USERNAME, COMMENT );

        Assert.assertNotNull( comment );
        Assert.assertEquals( COMMENT, comment.getComment() );
        Assert.assertEquals( CREATED_ON, comment.getCreatedOn() );
    }

    @Test( expected = UserNotMemberException.class )
    public void testCreateWhenUserNotMember() throws UserNotMemberException {
        tripCommentService.create( TRIP_ID, USERNAME, COMMENT );
    }

    @Test
    public void testGetAllByTripId() throws UserNotMemberException {
        Mockito.when( tripMemberServiceMock.isUserMember( Mockito.eq( TRIP_ID ), Mockito.eq( USERNAME ) ) )
               .thenReturn( true );

        Mockito.when( tripCommentsDaoMock.getAllByTripId( Mockito.eq( TRIP_ID ) ) )
               .thenReturn( Arrays.asList( TRIP_COMMENT ) );

        List<TripComment> comments = tripCommentService.getAllByTripId( TRIP_ID, USERNAME );

        Assert.assertNotNull( comments );
        Assert.assertFalse( comments.isEmpty() );
        Assert.assertEquals( 1, comments.size() );
    }

    @Test( expected = UserNotMemberException.class )
    public void testGetAllByTripIdWhenUserIsNotMember() throws UserNotMemberException {
        tripCommentService.getAllByTripId( TRIP_ID, USERNAME );
    }
}
