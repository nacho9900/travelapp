package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PlaceDao;
import ar.edu.itba.paw.model.Place;
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
public class PlaceJdbcDaoTest {


    private static final long tripId = 2;
    private static final long placeId = 3;
    private static final String googleIdFake = "fake google id";
    private static final String googleId = "This is a google place id test";
    private static final String name = "Magical place with psychedelic mushrooms and hookers";
    private static final double lat = 110;
    private static final double lng = -150;
    private static final String address = "Under the rabbit hole, to the left";

    @Autowired
    PlaceDao pd;

    @Test
    public void testCreate() {
        Place place = pd.create(googleId,name,lat,lng,address);
        Assert.assertNotNull(place);
        Assert.assertEquals(name, place.getName());
        Assert.assertEquals(address, place.getAddress());
    }

    @Test
    public void testFindById() {
        Optional<Place> placeOptional = pd.findById(placeId);
        Assert.assertTrue(placeOptional.isPresent());
        Assert.assertEquals(placeId, placeOptional.get().getId());
    }

    @Test
    public void testFindByGoogleId() {
        Optional<Place> placeOptional = pd.findByGoogleId(googleIdFake);
        Assert.assertTrue(placeOptional.isPresent());
        Assert.assertEquals(placeId, placeOptional.get().getId());
    }

    @Test
    public void testGetTripPlaces() {
        List<Place> places = pd.getTripPlaces(tripId);
        Assert.assertNotNull(places);
        Assert.assertTrue(!places.isEmpty());
        Assert.assertEquals(placeId, places.get(0).getId());
    }

}
