package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.TripMemberService;
import ar.edu.itba.paw.interfaces.TripPicturesDao;
import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripPicture;
import ar.edu.itba.paw.model.exception.EntityNotFoundException;
import ar.edu.itba.paw.model.exception.ImageFormatException;
import ar.edu.itba.paw.model.exception.ImageMaxSizeException;
import ar.edu.itba.paw.model.exception.UserNotOwnerOrAdminException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

@RunWith( MockitoJUnitRunner.class )
public class TestTripPictureServiceImpl
{
    @InjectMocks
    private TripPicturesServiceImpl tripPicturesService;

    @Mock
    private TripPicturesDao tripPicturesDaoMock;

    @Mock
    private TripMemberService tripMemberServiceMock;

    @Mock
    private TripService tripServiceMock;

    //TRIP
    private final long TRIP_ID = 1;
    private final String TRIP_NAME = "Trip to Constantinople";
    private final String TRIP_DESCRIPTION = "We are going to test Constantinople food and tour the markets.";
    private final LocalDate TRIP_START_DATE = LocalDate.of( 2021, 6, 1 );
    private final LocalDate TRIP_END_DATE = LocalDate.of( 2021, 6, 30 );

    private final Trip TRIP = new Trip( TRIP_ID, TRIP_NAME, TRIP_DESCRIPTION, TRIP_START_DATE, TRIP_END_DATE );

    //USER
    private final String USERNAME = "asdmm@asdddd.com";

    //Image
    private final long ID = 34;
    private String IMAGE_BASE_64;
    private final String IMAGE_BASE_64_BROKEN = "!";
    private final String NAME = "deadpool.png";
    private final String NAME_BROKEN = "sarasa";
    private byte[] IMAGE_BYTE_ARRAY;
    private TripPicture TRIP_PICTURE;
    private byte[] IMAGE_EMPTY_BYTE_ARRAY = new byte[1];
    private TripPicture TRIP_PICTURE_EMPTY = new TripPicture( ID, TRIP, NAME, IMAGE_EMPTY_BYTE_ARRAY );

    @Before
    public void before() throws IOException {
        InputStreamReader inputStreamReader;
        try ( InputStream inputStream = this.getClass().getResourceAsStream( "/test/image64" ) ) {
            inputStreamReader = new InputStreamReader( inputStream );
            IMAGE_BASE_64 = new BufferedReader( inputStreamReader ).lines().collect( Collectors.joining( "\n" ) );
        }

        ByteArrayOutputStream buffer;
        try ( InputStream imageInputStream = this.getClass().getResourceAsStream( "/test/image" ) ) {
            buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[4];
            while ( ( nRead = imageInputStream.read( data, 0, data.length ) ) != -1 ) {
                buffer.write( data, 0, nRead );
            }
        }
        buffer.flush();
        IMAGE_BYTE_ARRAY = buffer.toByteArray();
        TRIP_PICTURE = new TripPicture( ID, TRIP, NAME, IMAGE_BYTE_ARRAY );
    }

    @Test
    public void testCreate()
            throws ImageMaxSizeException, UserNotOwnerOrAdminException, EntityNotFoundException, ImageFormatException {
        Mockito.when( tripServiceMock.findById( Mockito.eq( TRIP_ID ) ) ).thenReturn( Optional.of( TRIP ) );
        Mockito.when( tripMemberServiceMock.isUserOwnerOrAdmin( Mockito.eq( TRIP_ID ), Mockito.eq( USERNAME ) ) )
               .thenReturn( true );
        Mockito.when( tripPicturesDaoMock.findByTripId( Mockito.eq( TRIP_ID ) ) ).thenReturn( Optional.empty() );
        Mockito.when(
                tripPicturesDaoMock.create( Mockito.eq( TRIP ), Mockito.eq( NAME ), Mockito.eq( IMAGE_BYTE_ARRAY ) ) )
               .thenReturn( TRIP_PICTURE );

        TripPicture tripPicture = tripPicturesService.createOrUpdate( TRIP_ID, NAME, IMAGE_BASE_64, USERNAME );

        Assert.assertNotNull( tripPicture );
        Assert.assertNotNull( tripPicture.getPicture() );
        Assert.assertArrayEquals( IMAGE_BYTE_ARRAY, tripPicture.getPicture() );
    }

    @Test
    public void testUpdate()
            throws ImageMaxSizeException, UserNotOwnerOrAdminException, EntityNotFoundException, ImageFormatException {
        Mockito.when( tripServiceMock.findById( Mockito.eq( TRIP_ID ) ) ).thenReturn( Optional.of( TRIP ) );
        Mockito.when( tripMemberServiceMock.isUserOwnerOrAdmin( Mockito.eq( TRIP_ID ), Mockito.eq( USERNAME ) ) )
               .thenReturn( true );
        Mockito.when( tripPicturesDaoMock.findByTripId( Mockito.eq( TRIP_ID ) ) )
               .thenReturn( Optional.of( TRIP_PICTURE_EMPTY ) );
        Mockito.when( tripPicturesDaoMock.update( Mockito.eq( TRIP_PICTURE_EMPTY ) ) ).thenReturn( TRIP_PICTURE );

        TripPicture tripPicture = tripPicturesService.createOrUpdate( TRIP_ID, NAME, IMAGE_BASE_64, USERNAME );

        Assert.assertNotNull( tripPicture );
        Assert.assertNotNull( tripPicture.getPicture() );
        Assert.assertArrayEquals( IMAGE_BYTE_ARRAY, tripPicture.getPicture() );
    }

    @Test( expected = ImageFormatException.class )
    public void testCreateOrUpdateWhenNameIsWrong()
            throws ImageMaxSizeException, UserNotOwnerOrAdminException, EntityNotFoundException, ImageFormatException {
        Mockito.when( tripServiceMock.findById( Mockito.eq( TRIP_ID ) ) ).thenReturn( Optional.of( TRIP ) );
        Mockito.when( tripMemberServiceMock.isUserOwnerOrAdmin( Mockito.eq( TRIP_ID ), Mockito.eq( USERNAME ) ) )
               .thenReturn( true );

        tripPicturesService.createOrUpdate( TRIP_ID, NAME_BROKEN, IMAGE_BASE_64, USERNAME );
    }

    @Test( expected = ImageFormatException.class )
    public void testCreateOrUpdateWhenImageIsWrong()
            throws ImageMaxSizeException, UserNotOwnerOrAdminException, EntityNotFoundException, ImageFormatException {
        Mockito.when( tripServiceMock.findById( Mockito.eq( TRIP_ID ) ) ).thenReturn( Optional.of( TRIP ) );
        Mockito.when( tripMemberServiceMock.isUserOwnerOrAdmin( Mockito.eq( TRIP_ID ), Mockito.eq( USERNAME ) ) )
               .thenReturn( true );

        tripPicturesService.createOrUpdate( TRIP_ID, NAME, IMAGE_BASE_64_BROKEN, USERNAME );
    }

    @Test( expected = UserNotOwnerOrAdminException.class )
    public void testCreateOrUpdateWhenUserIsNotOwnerOrAdmin()
            throws ImageMaxSizeException, UserNotOwnerOrAdminException, EntityNotFoundException, ImageFormatException {
        Mockito.when( tripServiceMock.findById( Mockito.eq( TRIP_ID ) ) ).thenReturn( Optional.of( TRIP ) );

        tripPicturesService.createOrUpdate( TRIP_ID, NAME, IMAGE_BASE_64, USERNAME );
    }

    @Test( expected = EntityNotFoundException.class )
    public void testCreateOrUpdateWhenTripNotExists()
            throws ImageMaxSizeException, UserNotOwnerOrAdminException, EntityNotFoundException, ImageFormatException {
        tripPicturesService.createOrUpdate( TRIP_ID, NAME, IMAGE_BASE_64, USERNAME );
    }
}
