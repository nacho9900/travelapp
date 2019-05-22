package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.ActivityDao;
import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Activity;
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
public class ActivityJdbcDaoTest {

    private static final String name = "name";
    private static final String category = "category";
    private static final long placeID = 3;

    private static final long ID = 4;
    private static final String name2 ="Scuba diving";
    private static final String category2 = "Sports";




    @Autowired
    ActivityDao ad;

    @Test
    public void testCreate() {
        Activity activity = ad.create(name,category,placeID);
        Assert.assertNotNull(activity);
        Assert.assertEquals(name, activity.getName());
        Assert.assertEquals(placeID, activity.getPlaceId());
    }

    @Test
    public void testfindById() {
        Optional<Activity> activity = ad.findById(ID);
        Assert.assertTrue(activity.isPresent());
        Assert.assertEquals(name2, activity.get().getName());

    }

    @Test
    public void testfindByName() {
        Optional<Activity> activity = ad.findByName(name2);
        Assert.assertTrue(activity.isPresent());
        Assert.assertEquals(name2, activity.get().getName());
    }

    @Test
    public void testfindByCategory(){
        Optional<Activity> activity = ad.findByCategory(category2);
        Assert.assertTrue(activity.isPresent());
        Assert.assertEquals(category2, activity.get().getCategory());
    }

    @Test
    public void testgetActivityPlace() {
        Optional<Place> place = ad.getActivityPlace(ID);
        Assert.assertTrue(place.isPresent());
        Assert.assertEquals(placeID, place.get().getId());
    }

}
