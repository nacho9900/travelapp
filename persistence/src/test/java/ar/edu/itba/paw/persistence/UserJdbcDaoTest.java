package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import javax.sql.DataSource;
import java.util.Optional;


import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;



@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserJdbcDaoTest {
    private static final String PASSWORD = "Password";
    private static final String EMAIL = "test@mail.com";
    private static final String LASTNAME = "lastname";
    private static final String FIRSTNAME = "firstname";
    private static final long ID = 1;

    @Autowired
    private DataSource ds;

    @Autowired
    private UserJdbcDao userDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
    }

    @Test
    public void testCreate() {
        final User user = userDao.create(FIRSTNAME, LASTNAME, EMAIL, PASSWORD);
        assertNotNull(user);
        assertEquals(EMAIL, user.getEmail());
        assertEquals(PASSWORD, user.getPassword());
        assertEquals(FIRSTNAME, user.getFirstname());
        assertEquals(LASTNAME, user.getLastname());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
    }

/*
    @Test
    public void TestfindById() {
        Optional<User> user = userDao.findById(ID);
        assertNotNull(user.get());
        assertEquals(EMAIL, user.get().getEmail());
        assertEquals(PASSWORD, user.get().getPassword());
        assertEquals(FIRSTNAME, user.get().getFirstname());
        assertEquals(LASTNAME, user.get().getLastname());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));

    }*/
/*

    @Test
    public void TestfindByUsername() {
        Optional<User> user = userDao.findByUsername(EMAIL);
        assertNotNull(user.get());
        assertEquals(EMAIL, user.get().getEmail());
        assertEquals(PASSWORD, user.get().getPassword());
        assertEquals(FIRSTNAME, user.get().getFirstname());
        assertEquals(LASTNAME, user.get().getLastname());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));

    }
*/


}