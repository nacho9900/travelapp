package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.ActivityDao;
import ar.edu.itba.paw.interfaces.TripActivitiesDao;
import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripActivity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;



@Transactional
@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class TripActivitiesJdbcDaoTest {


    @Autowired
    TripActivitiesDao tad;

    private static final long tripId = 2;
    private static final long placeId = 3;
    private static final long activityId = 4;

    private static final long ID = 1;



    @Test
    public void testCreate() {
        TripActivity tripact = tad.create(tripId,placeId,activityId);
        Assert.assertNotNull(tripact);
        Assert.assertEquals(tripId, tripact.getTripId());
        Assert.assertEquals(placeId, tripact.getPlaceId());
    }

    @Test
    public void testfindById() {
        Optional<TripActivity> tripact = tad.findById(ID);
        Assert.assertTrue(tripact.isPresent());
        Assert.assertEquals(tripId, tripact.get().getTripId());
        Assert.assertEquals(placeId, tripact.get().getPlaceId());

    }
}
