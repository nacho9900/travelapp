package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripUsersDao;
import ar.edu.itba.paw.model.TripUser;
import ar.edu.itba.paw.model.UserRole;
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
public class TripUsersJdbcDaoTest {

    @Autowired
    TripUsersDao tud;


    private static final long tripId = 2;
    private static final long userId = 1;
    private static final UserRole role = UserRole.ADMIN;


    private static final long ID = 1;

    @Test
    public void testCreate() {
        TripUser tripUser = tud.create(tripId,userId,role);
        Assert.assertNotNull(tripUser);
        Assert.assertEquals(tripId, tripUser.getTripId());
        Assert.assertEquals(userId, tripUser.getUserId());
    }

    @Test
    public void testfindById() {
        Optional<TripUser> tripUser = tud.findById(ID);
        Assert.assertTrue(tripUser.isPresent());
        Assert.assertEquals(tripId, tripUser.get().getTripId());
        Assert.assertEquals(userId, tripUser.get().getUserId());

    }

    @Test
    public void testfindByTripId(){
        List<TripUser> tripPlace = tud.findByTripId(tripId);
        Assert.assertNotNull(tripPlace);
        Assert.assertTrue(!tripPlace.isEmpty());
        Assert.assertEquals(tripId, tripPlace.get(0).getTripId());

    }

    @Test
    public void testfindByUserId() {
        List<TripUser> tripPlace = tud.findByUserId(userId);
        Assert.assertNotNull(tripPlace);
        Assert.assertTrue(!tripPlace.isEmpty());
        Assert.assertEquals(userId, tripPlace.get(0).getUserId());

    }
}
