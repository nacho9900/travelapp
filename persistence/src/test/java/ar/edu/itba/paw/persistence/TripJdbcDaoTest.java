package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.interfaces.TripDao;
import ar.edu.itba.paw.model.Trip;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Transactional
@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class TripJdbcDaoTest {

    @Autowired
    TripDao td;

    private static final long tripId = 2;
    private static final long userId = 1;
    private static final long placeId = 3;

    private static final String name = "Triping under the stars";
    private static final String description = "Lucy in the sky with diamonds";
    private static final Calendar startDate = Calendar.getInstance();
    private static final Calendar endDate = Calendar.getInstance();


    @Test
    public void testCreate() {
        Trip trip = td.create(placeId,name,description,startDate,endDate);
        Assert.assertNotNull(trip);
        Assert.assertEquals(name, trip.getName());
        Assert.assertEquals(placeId, trip.getStartPlaceId());
    }

    @Test
    public void testFindById() {
        Optional<Trip> tripOptional = td.findById(tripId);
        Assert.assertTrue(tripOptional.isPresent());
        Assert.assertEquals(tripId, tripOptional.get().getId());

    }

    /*@Test HSQLD DOESNT SUPPORT ILIKE OPERATION
    public void testFindByName() {
        List<Trip> trips = td.findByName(name);
        Assert.assertNotNull(trips);
        Assert.assertTrue(!trips.isEmpty());
        Assert.assertEquals(tripId, trips.get(0).getId());
    }*/

    @Test
    public void testFindUserTrips(){
        List<Trip> trips = td.findUserTrips(userId, 1);
        Assert.assertNotNull(trips);
        Assert.assertTrue(!trips.isEmpty());
        Assert.assertEquals(tripId, trips.get(0).getId());
    }

    @Test
    public void testUserIsAdmin() {
        boolean bool = td.userIsAdmin(userId, tripId);
        Assert.assertTrue(bool);
    }

    @Test
    public void testIsTravelling() {
        boolean bool = td.isTravelling(userId, tripId);
        Assert.assertTrue(bool);
    }
    @Test
    public void testGetAllTrips() {
        List<Trip> trips = td.getAllTrips();
        Assert.assertNotNull(trips);
        Assert.assertTrue(!trips.isEmpty());
    }
    @Test
    public void testCountUserTrips() {
        int count = td.countUserTrips(userId);
        Assert.assertEquals(1, count);
    }

}
