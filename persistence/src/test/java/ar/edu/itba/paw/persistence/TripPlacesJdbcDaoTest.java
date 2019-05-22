package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripPlacesDao;
import ar.edu.itba.paw.model.TripPlace;
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
public class TripPlacesJdbcDaoTest {

    @Autowired
    TripPlacesDao tpd;

    private static final long tripId = 2;
    private static final long placeId = 3;

    private static final long ID = 1;

    @Test
    public void testCreate() {
        TripPlace tripPlace = tpd.create(tripId,placeId);
        Assert.assertNotNull(tripPlace);
        Assert.assertEquals(tripId, tripPlace.getTripId());
        Assert.assertEquals(placeId, tripPlace.getPlaceId());
    }

    @Test
    public void testfindById() {
        Optional<TripPlace> tripPlace = tpd.findById(ID);
        Assert.assertTrue(tripPlace.isPresent());
        Assert.assertEquals(tripId, tripPlace.get().getTripId());
        Assert.assertEquals(placeId, tripPlace.get().getPlaceId());

    }

    @Test
    public void testfindByTripId(){
        List<TripPlace> tripPlace = tpd.findByTripId(tripId);
        Assert.assertNotNull(tripPlace);
        Assert.assertTrue(!tripPlace.isEmpty());
        Assert.assertEquals(tripId, tripPlace.get(0).getTripId());

    }

    @Test
    public void testfindByPlaceId() {
        List<TripPlace> tripPlace = tpd.findByPlaceId(placeId);
        Assert.assertNotNull(tripPlace);
        Assert.assertTrue(!tripPlace.isEmpty());
        Assert.assertEquals(placeId, tripPlace.get(0).getPlaceId());

    }
}
