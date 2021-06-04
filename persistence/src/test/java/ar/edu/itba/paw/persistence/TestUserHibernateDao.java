package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@RunWith( SpringJUnit4ClassRunner.class )
@Sql( "classpath:schema.sql" )
@ContextConfiguration( classes = TestConfig.class )
@Transactional
public class TestUserHibernateDao
{
    @Autowired
    private UserHibernateDao ud;

    @Test
    public void testCreate() {
        final User user = ud.create( "Carlo", "Magno", "carlomagno@gmail.com", "carlomagno95",
                                     LocalDate.of( 768, 4, 2 ), "AR", "", UUID.randomUUID() );
        Assert.assertNotNull( user );
    }
}
