package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PlaceDao;
import ar.edu.itba.paw.interfaces.TripDao;
import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Trip;
import org.hibernate.Hibernate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TestTripActivityHibernateDao
{
    @Autowired
    private ActivityHibernateDao activityDao;

    @Autowired
    private PlaceDao placeDao;

    @Autowired
    private TripDao tripDao;

    /* create */

    private final long TRIP_ID_CREATE = 2;
    private final long PLACE_ID_CREATE = 2;
    private Place PLACE_CREATE;
    private Trip TRIP_CREATE;
    private final String NAME_CREATE = "molto bene+{}<@#";
    private final LocalDate START_DATE_CREATE = LocalDate.of( 2022, 2, 2 );
    private final LocalDate END_DATE_CREATE = LocalDate.of( 2022, 2, 10 );

    /* find */
    private final long ID_FIND = 1;
    private final String NAME_FIND = "Conocer el Obelisco";
    private final LocalDate START_DATE_FIND = LocalDate.of( 2021, 6, 2 );
    private final LocalDate END_DATE_FIND = LocalDate.of( 2021, 6, 3 );
    private final long TRIP_ID_FIND = 1;
    private final long PLACE_ID_FIND = 1;
    private Place PLACE_FIND;
    private Trip TRIP_FIND;

    private final long ID_FIND2 = 2;


    @Before
    public void before() {
        PLACE_FIND = placeDao.findById( PLACE_ID_FIND )
                             .get();
        TRIP_FIND = tripDao.findById( TRIP_ID_FIND )
                           .get();
        PLACE_CREATE = placeDao.findById( PLACE_ID_CREATE )
                               .get();
        TRIP_CREATE = tripDao.findById( TRIP_ID_CREATE )
                             .get();
    }

    @Test
    public void testFindById() {
        final Optional<Activity> maybeActivity = activityDao.findById( ID_FIND );
        Assert.assertNotNull( maybeActivity );
        Assert.assertTrue( maybeActivity.isPresent() );
        final Activity activity = maybeActivity.get();
        Assert.assertEquals( ID_FIND, activity.getId() );
        Assert.assertEquals( NAME_FIND, activity.getName() );
        Assert.assertEquals( START_DATE_FIND, activity.getStartDate() );
        Assert.assertEquals( END_DATE_FIND, activity.getEndDate() );
        Hibernate.initialize( activity.getTrip() );
        Assert.assertEquals( TRIP_FIND, activity.getTrip() );
        Assert.assertEquals( PLACE_FIND, activity.getPlace() );
    }

    @Test
    public void testIsActivityPartOfTheTrip() {
        Assert.assertTrue( activityDao.isActivityPartOfTheTrip( TRIP_ID_FIND, ID_FIND ) );
        Assert.assertFalse( activityDao.isActivityPartOfTheTrip( TRIP_ID_FIND, ID_FIND2 ) );
    }

    @Test
    public void testCreate() {
        final Activity activity = activityDao.create( NAME_CREATE, TRIP_CREATE, START_DATE_CREATE, END_DATE_CREATE,
                                                      PLACE_CREATE );
        Assert.assertNotNull( activity );
        Assert.assertTrue( activity.getId() > 0 );
        Assert.assertEquals( NAME_CREATE, activity.getName() );
        Assert.assertEquals( START_DATE_CREATE, activity.getStartDate() );
        Assert.assertEquals( END_DATE_CREATE, activity.getEndDate() );
        Hibernate.initialize( activity.getTrip() );
        Assert.assertEquals( TRIP_CREATE, activity.getTrip() );
        Assert.assertEquals( PLACE_CREATE, activity.getPlace() );
    }
}
