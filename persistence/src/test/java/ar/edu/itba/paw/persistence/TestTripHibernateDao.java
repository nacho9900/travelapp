package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Trip;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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
}
