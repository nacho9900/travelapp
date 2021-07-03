package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.ActivityDao;
import ar.edu.itba.paw.interfaces.PlaceService;
import ar.edu.itba.paw.interfaces.TripMemberService;
import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.exception.ActivityNotPartOfTheTripException;
import ar.edu.itba.paw.model.exception.EntityNotFoundException;
import ar.edu.itba.paw.model.exception.InvalidDateRangeException;
import ar.edu.itba.paw.model.exception.UserNotOwnerOrAdminException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Optional;

@RunWith( MockitoJUnitRunner.class )
public class TestActivityServiceImpl
{
    @InjectMocks
    private ActivityServiceImpl activityService;

    @Mock
    private ActivityDao activityDaoMock;

    @Mock
    private TripService tripServiceMock;

    @Mock
    private TripMemberService tripMemberServiceMock;

    @Mock
    private PlaceService placeServiceMock;

    //PLACE
    private final long PLACE_ID = 23;
    private final String PLACE_GOOGLE_ID = "asd";
    private final double PLACE_LAT = 0.0;
    private final double PLACE_LNG = 0.0;
    private final String PLACE_NAME = "aasdd";
    private final String PLACE_ADDRESS = "asd 123";

    private final Place PLACE = new Place( PLACE_ID, PLACE_GOOGLE_ID, PLACE_NAME, PLACE_LAT, PLACE_LNG, PLACE_ADDRESS );

    //TRIP
    private final long TRIP_ID = 1;
    private final String TRIP_NAME = "Trip to Constantinople";
    private final String TRIP_DESCRIPTION = "We are going to test Constantinople food and tour the markets.";
    private final LocalDate TRIP_START_DATE = LocalDate.of( 2021, 6, 1 );
    private final LocalDate TRIP_END_DATE = LocalDate.of( 2021, 6, 30 );

    private final Trip TRIP = new Trip( TRIP_ID, TRIP_NAME, TRIP_DESCRIPTION, TRIP_START_DATE, TRIP_END_DATE );

    //ACTIVITY
    private final long ID = 1;
    private final String NAME = "TEST NAME";
    private final LocalDate START_DATE_OK = LocalDate.of( 2021, 6, 1 );
    private final LocalDate END_DATE_OK = LocalDate.of( 2021, 6, 30 );
    private final LocalDate START_DATE_WRONG = LocalDate.of( 2020, 6, 1 );
    private final LocalDate END_DATE_WRONG = LocalDate.of( 2020, 6, 30 );
    private final Activity ACTIVITY = new Activity( ID, NAME, PLACE, TRIP, START_DATE_OK, END_DATE_OK );

    //USER
    private final String USERNAME = "asdasd@test.com";

    @Test
    public void testCreate() throws EntityNotFoundException, UserNotOwnerOrAdminException, InvalidDateRangeException {
        Mockito.when( tripServiceMock.findById( Mockito.eq( TRIP_ID ) ) ).thenReturn( Optional.of( TRIP ) );

        Mockito.when( tripMemberServiceMock.isUserOwnerOrAdmin( Mockito.eq( TRIP_ID ), Mockito.eq( USERNAME ) ) )
               .thenReturn( true );

        Mockito.when( placeServiceMock.createIfNotExists( Mockito.eq( PLACE_GOOGLE_ID ), Mockito.eq( PLACE_NAME ),
                                                          Mockito.eq( PLACE_LAT ), Mockito.eq( PLACE_LNG ),
                                                          Mockito.eq( PLACE_ADDRESS ) ) ).thenReturn( PLACE );

        Mockito.when( activityDaoMock.create( Mockito.eq( NAME ), Mockito.eq( TRIP ), Mockito.eq( START_DATE_OK ),
                                              Mockito.eq( END_DATE_OK ), Mockito.eq( PLACE ) ) ).thenReturn( ACTIVITY );

        Activity activity = activityService.create( TRIP_ID, NAME, START_DATE_OK, END_DATE_OK, PLACE, USERNAME );

        Assert.assertNotNull( activity );
        Assert.assertNotNull( activity.getPlace() );
        Assert.assertEquals( TRIP, activity.getTrip() );
    }

    @Test( expected = InvalidDateRangeException.class )
    public void testCreateWhenStartDateAreInvalid()
            throws EntityNotFoundException, UserNotOwnerOrAdminException, InvalidDateRangeException {
        Mockito.when( tripServiceMock.findById( Mockito.eq( TRIP_ID ) ) ).thenReturn( Optional.of( TRIP ) );

        Mockito.when( tripMemberServiceMock.isUserOwnerOrAdmin( Mockito.eq( TRIP_ID ), Mockito.eq( USERNAME ) ) )
               .thenReturn( true );

        activityService.create( TRIP_ID, NAME, START_DATE_WRONG, END_DATE_OK, PLACE, USERNAME );
    }

    @Test( expected = InvalidDateRangeException.class )
    public void testCreateWhenEndDateAreInvalid()
            throws EntityNotFoundException, UserNotOwnerOrAdminException, InvalidDateRangeException {
        Mockito.when( tripServiceMock.findById( Mockito.eq( TRIP_ID ) ) ).thenReturn( Optional.of( TRIP ) );

        Mockito.when( tripMemberServiceMock.isUserOwnerOrAdmin( Mockito.eq( TRIP_ID ), Mockito.eq( USERNAME ) ) )
               .thenReturn( true );

        activityService.create( TRIP_ID, NAME, START_DATE_OK, END_DATE_WRONG, PLACE, USERNAME );
    }

    @Test( expected = UserNotOwnerOrAdminException.class )
    public void testCreateWhenUserIsNotOwnerOrAdmin()
            throws EntityNotFoundException, UserNotOwnerOrAdminException, InvalidDateRangeException {
        Mockito.when( tripServiceMock.findById( Mockito.eq( TRIP_ID ) ) ).thenReturn( Optional.of( TRIP ) );

        Mockito.when( tripMemberServiceMock.isUserOwnerOrAdmin( Mockito.eq( TRIP_ID ), Mockito.eq( USERNAME ) ) )
               .thenReturn( false );

        activityService.create( TRIP_ID, NAME, START_DATE_WRONG, END_DATE_OK, PLACE, USERNAME );
    }

    @Test( expected = EntityNotFoundException.class )
    public void testCreateWhenTripNotExists()
            throws EntityNotFoundException, UserNotOwnerOrAdminException, InvalidDateRangeException {
        Mockito.when( tripServiceMock.findById( Mockito.eq( TRIP_ID ) ) ).thenReturn( Optional.empty() );

        activityService.create( TRIP_ID, NAME, START_DATE_WRONG, END_DATE_OK, PLACE, USERNAME );
    }

    @Test
    public void testUpdate()
            throws ActivityNotPartOfTheTripException, EntityNotFoundException, UserNotOwnerOrAdminException,
            InvalidDateRangeException {
        Mockito.when( tripServiceMock.findById( Mockito.eq( TRIP_ID ) ) ).thenReturn( Optional.of( TRIP ) );

        Mockito.when( activityDaoMock.findById( Mockito.eq( ID ) ) ).thenReturn( Optional.of( ACTIVITY ) );

        Mockito.when( tripMemberServiceMock.isUserOwnerOrAdmin( Mockito.eq( TRIP_ID ), Mockito.eq( USERNAME ) ) )
               .thenReturn( true );

        Mockito.when( placeServiceMock.createIfNotExists( Mockito.eq( PLACE_GOOGLE_ID ), Mockito.eq( PLACE_NAME ),
                                                          Mockito.eq( PLACE_LAT ), Mockito.eq( PLACE_LNG ),
                                                          Mockito.eq( PLACE_ADDRESS ) ) ).thenReturn( PLACE );

        Mockito.when( activityDaoMock.isActivityPartOfTheTrip( Mockito.eq( TRIP_ID ), Mockito.eq( ID ) ) )
               .thenReturn( true );


        Mockito.when( activityDaoMock.update( Mockito.eq( ACTIVITY ) ) ).thenReturn( ACTIVITY );

        Activity activity = activityService.update( ID, TRIP_ID, NAME, START_DATE_OK, END_DATE_OK, PLACE, USERNAME );

        Assert.assertNotNull( activity );
    }

    @Test( expected = InvalidDateRangeException.class )
    public void testUpdateWhenStartDateIsInvalid()
            throws ActivityNotPartOfTheTripException, EntityNotFoundException, UserNotOwnerOrAdminException,
            InvalidDateRangeException {
        Mockito.when( tripServiceMock.findById( Mockito.eq( TRIP_ID ) ) ).thenReturn( Optional.of( TRIP ) );

        Mockito.when( activityDaoMock.findById( Mockito.eq( ID ) ) ).thenReturn( Optional.of( ACTIVITY ) );

        Mockito.when( tripMemberServiceMock.isUserOwnerOrAdmin( Mockito.eq( TRIP_ID ), Mockito.eq( USERNAME ) ) )
               .thenReturn( true );

        Mockito.when( activityDaoMock.isActivityPartOfTheTrip( Mockito.eq( TRIP_ID ), Mockito.eq( ID ) ) )
               .thenReturn( true );

        activityService.update( ID, TRIP_ID, NAME, START_DATE_WRONG, END_DATE_OK, PLACE, USERNAME );
    }

    @Test( expected = UserNotOwnerOrAdminException.class )
    public void testUpdateWhenUserIsNotOwnerOrAdmin()
            throws ActivityNotPartOfTheTripException, EntityNotFoundException, UserNotOwnerOrAdminException,
            InvalidDateRangeException {
        Mockito.when( tripServiceMock.findById( Mockito.eq( TRIP_ID ) ) ).thenReturn( Optional.of( TRIP ) );

        Mockito.when( activityDaoMock.findById( Mockito.eq( ID ) ) ).thenReturn( Optional.of( ACTIVITY ) );

        Mockito.when( activityDaoMock.isActivityPartOfTheTrip( Mockito.eq( TRIP_ID ), Mockito.eq( ID ) ) )
               .thenReturn( true );

        activityService.update( ID, TRIP_ID, NAME, START_DATE_OK, END_DATE_OK, PLACE, USERNAME );
    }

    @Test( expected = ActivityNotPartOfTheTripException.class )
    public void testUpdateWhenActivityIsNotPartOfTheTrip()
            throws ActivityNotPartOfTheTripException, EntityNotFoundException, UserNotOwnerOrAdminException,
            InvalidDateRangeException {
        Mockito.when( tripServiceMock.findById( Mockito.eq( TRIP_ID ) ) ).thenReturn( Optional.of( TRIP ) );

        Mockito.when( activityDaoMock.findById( Mockito.eq( ID ) ) ).thenReturn( Optional.of( ACTIVITY ) );

        activityService.update( ID, TRIP_ID, NAME, START_DATE_OK, END_DATE_OK, PLACE, USERNAME );
    }

    @Test( expected = EntityNotFoundException.class )
    public void testUpdateWhenActivityNotExists()
            throws ActivityNotPartOfTheTripException, EntityNotFoundException, UserNotOwnerOrAdminException,
            InvalidDateRangeException {
        Mockito.when( tripServiceMock.findById( Mockito.eq( TRIP_ID ) ) ).thenReturn( Optional.of( TRIP ) );

        Mockito.when( activityDaoMock.findById( Mockito.eq( ID ) ) ).thenReturn( Optional.empty() );

        activityService.update( ID, TRIP_ID, NAME, START_DATE_OK, END_DATE_OK, PLACE, USERNAME );
    }

    @Test( expected = EntityNotFoundException.class )
    public void testUpdateWhenTripNotExists()
            throws ActivityNotPartOfTheTripException, EntityNotFoundException, UserNotOwnerOrAdminException,
            InvalidDateRangeException {
        Mockito.when( tripServiceMock.findById( Mockito.eq( TRIP_ID ) ) ).thenReturn( Optional.empty() );

        activityService.update( ID, TRIP_ID, NAME, START_DATE_OK, END_DATE_OK, PLACE, USERNAME );
    }

    @Test
    public void testDelete()
            throws ActivityNotPartOfTheTripException, EntityNotFoundException, UserNotOwnerOrAdminException {
        Mockito.when( activityDaoMock.findById( Mockito.eq( ID ) ) ).thenReturn( Optional.of( ACTIVITY ) );

        Mockito.when( tripMemberServiceMock.isUserOwnerOrAdmin( Mockito.eq( TRIP_ID ), Mockito.eq( USERNAME ) ) )
               .thenReturn( true );

        Mockito.when( activityDaoMock.isActivityPartOfTheTrip( Mockito.eq( TRIP_ID ), Mockito.eq( ID ) ) )
               .thenReturn( true );

        activityService.delete( ID, TRIP_ID, USERNAME );
    }

    @Test( expected = UserNotOwnerOrAdminException.class )
    public void testDeleteWhenUserIsNotOwnerOrAdmin()
            throws ActivityNotPartOfTheTripException, EntityNotFoundException, UserNotOwnerOrAdminException {
        Mockito.when( activityDaoMock.findById( Mockito.eq( ID ) ) ).thenReturn( Optional.of( ACTIVITY ) );

        Mockito.when( activityDaoMock.isActivityPartOfTheTrip( Mockito.eq( TRIP_ID ), Mockito.eq( ID ) ) )
               .thenReturn( true );

        activityService.delete( ID, TRIP_ID, USERNAME );
    }

    @Test( expected = ActivityNotPartOfTheTripException.class )
    public void testDeleteWhenActivityIsNotPartOfTheTrip()
            throws ActivityNotPartOfTheTripException, EntityNotFoundException, UserNotOwnerOrAdminException {
        Mockito.when( activityDaoMock.findById( Mockito.eq( ID ) ) ).thenReturn( Optional.of( ACTIVITY ) );

        activityService.delete( ID, TRIP_ID, USERNAME );
    }

    @Test( expected = EntityNotFoundException.class )
    public void testDeleteWhenActivityDoestExists()
            throws ActivityNotPartOfTheTripException, EntityNotFoundException, UserNotOwnerOrAdminException {
        Mockito.when( activityDaoMock.findById( Mockito.eq( ID ) ) ).thenReturn( Optional.empty() );

        activityService.delete( ID, TRIP_ID, USERNAME );
    }
}
