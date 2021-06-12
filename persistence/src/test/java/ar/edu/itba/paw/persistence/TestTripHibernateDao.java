package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.PaginatedResult;
import ar.edu.itba.paw.model.Trip;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@RunWith( SpringJUnit4ClassRunner.class )
@Sql( "classpath:schema.sql" )
@ContextConfiguration( classes = TestConfig.class )
@Transactional
public class TestTripHibernateDao
{
    @Autowired
    private TripHibernateDao tripDao;

    /* test create */
    private final String NAME = "Viaje a Las Toninas";
    private final String DESCRIPTION = "Vamos a la playa, llevar reposera y sombrillas @áááñññ!!!";
    private final LocalDate START_DATE = LocalDate.of( 2021, 6, 1 );
    private final LocalDate END_DATE = LocalDate.of( 2021, 6, 7 );

    /* test find */
    private final long USER_ID_1 = 1;
    private final String USERNAME_1 = "inegro@itba.edu.ar";
    private final long USER_ID_2 = 2;
    private final String USERNAME_2 = "jdoe@itba.edu.ar";
    private final int PAGE = 1;
    private final int PER_PAGE = 20;
    private final int TRIPS_QUANTITY = 1;
    private final long ID_FIND = 1;
    private final String NAME_FIND = "Buenos Aires 2022";
    private final String DESCRIPTION_FIND = "Recorrido por la Ciudad";
    private final LocalDate START_DATE_FIND = LocalDate.of( 2021, 6, 1 );
    private final LocalDate END_DATE_FIND = LocalDate.of( 2021, 6, 30 );
    private final double LAT_FIND = -34.6036;
    private final double LNG_FIND = -58.3815;

    /* test update */
    private final String NAME_NEW = "Barcelona 2022";

    @Test
    public void testCreate() {
        final Trip trip = tripDao.create( NAME, DESCRIPTION, START_DATE, END_DATE );
        Assert.assertNotNull( trip );
        Assert.assertTrue( trip.getId() > 0 );
        Assert.assertEquals( NAME, trip.getName() );
        Assert.assertEquals( DESCRIPTION, trip.getDescription() );
        Assert.assertEquals( START_DATE, trip.getStartDate() );
        Assert.assertEquals( END_DATE, trip.getEndDate() );
    }

    @Test
    public void testFindUserTripsWhenUserHasTrips() {
        final PaginatedResult<Trip> trips = tripDao.findUserTrips( USER_ID_1, PAGE, PER_PAGE );
        Assert.assertNotNull( trips );
        Assert.assertFalse( trips.isEmpty() );
        Assert.assertFalse( trips.hasNextPage() );
        Assert.assertEquals( TRIPS_QUANTITY, trips.getResult()
                                                  .size() );
        final Trip trip = trips.getResult()
                               .stream()
                               .findFirst()
                               .get();
        Assert.assertNotNull( trip );
        Assert.assertEquals( ID_FIND, trip.getId() );
        Assert.assertEquals( NAME_FIND, trip.getName() );
        Assert.assertEquals( DESCRIPTION_FIND, trip.getDescription() );
        Assert.assertEquals( START_DATE_FIND, trip.getStartDate() );
        Assert.assertEquals( END_DATE_FIND, trip.getEndDate() );
    }

    @Test
    public void testFindUserTripsWhenUserDoestHasTrips() {
        final PaginatedResult<Trip> trips = tripDao.findUserTrips( USER_ID_2, PAGE, PER_PAGE );
        Assert.assertNotNull( trips );
        Assert.assertTrue( trips.isEmpty() );
    }

    @Test
    public void testFindById() {
        final Optional<Trip> maybeTrip = tripDao.findById( ID_FIND );
        Assert.assertNotNull( maybeTrip );
        Assert.assertTrue( maybeTrip.isPresent() );
        final Trip trip = maybeTrip.get();
        Assert.assertEquals( ID_FIND, trip.getId() );
        Assert.assertEquals( NAME_FIND, trip.getName() );
        Assert.assertEquals( DESCRIPTION_FIND, trip.getDescription() );
        Assert.assertEquals( START_DATE_FIND, trip.getStartDate() );
        Assert.assertEquals( END_DATE_FIND, trip.getEndDate() );
    }

    @Test
    public void testIsUserMember() {
        Assert.assertTrue( tripDao.isUserMember( ID_FIND, USERNAME_1 ) );
        Assert.assertFalse( tripDao.isUserMember( ID_FIND, USERNAME_2 ) );
    }

    @Test
    public void testIsUserOwnerOrAdmin() {
        Assert.assertTrue( tripDao.isUserOwnerOrAdmin( ID_FIND, USERNAME_1 ) );
        Assert.assertFalse( tripDao.isUserOwnerOrAdmin( ID_FIND, USERNAME_2 ) );
    }

    @Test
    public void testSearchWithLocation() {
        final PaginatedResult<Trip> trips = tripDao.search( null, LAT_FIND, LNG_FIND, null, null, PAGE, PER_PAGE );
        Assert.assertNotNull( trips );
        Assert.assertFalse( trips.isEmpty() );
        Assert.assertFalse( trips.hasNextPage() );
        Assert.assertEquals( TRIPS_QUANTITY, trips.getResult()
                                                  .size() );
        final Trip trip = trips.getResult()
                               .stream()
                               .findFirst()
                               .get();
        Assert.assertNotNull( trip );
        Assert.assertEquals( ID_FIND, trip.getId() );
        Assert.assertEquals( NAME_FIND, trip.getName() );
        Assert.assertEquals( DESCRIPTION_FIND, trip.getDescription() );
        Assert.assertEquals( START_DATE_FIND, trip.getStartDate() );
        Assert.assertEquals( END_DATE_FIND, trip.getEndDate() );
    }

    @Test
    public void testSearchWithDates() {
        final PaginatedResult<Trip> trips = tripDao.search( null, null, null, START_DATE_FIND, END_DATE_FIND, PAGE,
                                                            PER_PAGE );
        Assert.assertNotNull( trips );
        Assert.assertFalse( trips.isEmpty() );
        Assert.assertFalse( trips.hasNextPage() );
        Assert.assertEquals( TRIPS_QUANTITY, trips.getResult()
                                                  .size() );
        final Trip trip = trips.getResult()
                               .stream()
                               .findFirst()
                               .get();
        Assert.assertNotNull( trip );
        Assert.assertEquals( ID_FIND, trip.getId() );
        Assert.assertEquals( NAME_FIND, trip.getName() );
        Assert.assertEquals( DESCRIPTION_FIND, trip.getDescription() );
        Assert.assertEquals( START_DATE_FIND, trip.getStartDate() );
        Assert.assertEquals( END_DATE_FIND, trip.getEndDate() );
    }

    @Test
    public void testSearchWithText() {
        final PaginatedResult<Trip> trips = tripDao.search( "B", null, null, null, null, PAGE,
                                                            PER_PAGE );
        Assert.assertNotNull( trips );
        Assert.assertFalse( trips.isEmpty() );
        Assert.assertFalse( trips.hasNextPage() );
        Assert.assertEquals( TRIPS_QUANTITY, trips.getResult()
                                                  .size() );
        final Trip trip = trips.getResult()
                               .stream()
                               .findFirst()
                               .get();
        Assert.assertNotNull( trip );
        Assert.assertEquals( ID_FIND, trip.getId() );
        Assert.assertEquals( NAME_FIND, trip.getName() );
        Assert.assertEquals( DESCRIPTION_FIND, trip.getDescription() );
        Assert.assertEquals( START_DATE_FIND, trip.getStartDate() );
        Assert.assertEquals( END_DATE_FIND, trip.getEndDate() );
    }

    @Test
    public void testUpdate() {
        final Optional<Trip> maybeTrip = tripDao.findById( ID_FIND );
        Assert.assertTrue( maybeTrip.isPresent() );
        Trip trip = maybeTrip.get();
        trip.setName( NAME_NEW );
        trip = tripDao.update( trip );
        Assert.assertEquals( ID_FIND, trip.getId() );
        Assert.assertEquals( NAME_NEW, trip.getName() );
        Assert.assertEquals( DESCRIPTION_FIND, trip.getDescription() );
        Assert.assertEquals( START_DATE_FIND, trip.getStartDate() );
        Assert.assertEquals( END_DATE_FIND, trip.getEndDate() );
    }

}
