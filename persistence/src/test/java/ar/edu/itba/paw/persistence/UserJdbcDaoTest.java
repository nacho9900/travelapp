package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;


import javax.sql.DataSource;
import java.util.Optional;

@Transactional
@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserJdbcDaoTest {
    private static final String PASSWORD = "password2";

    private static final String LASTNAME = "lastname2";
    private static final String FIRSTNAME = "firstname2";

    private static final String EMAIL = "test@mail.com";
    private static final String EMAIL2 = "test@mail.com2";

    private static final long ID = 1;
    private static final long ID2 = 2;

    @Autowired
    private DataSource ds;

    @Autowired
    private UserJdbcDao userDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testCreate() {
        final User user = userDao.create(FIRSTNAME, LASTNAME, EMAIL2, PASSWORD);
        Assert.assertNotNull(user);
        Assert.assertEquals(ID2, user.getId());
        Assert.assertEquals(LASTNAME, user.getLastname());
        JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,"users","firstname = " + "'" + FIRSTNAME + "'");
    }

    @Test
    public void TestfindById() {
        Optional<User> user = userDao.findById(ID);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(ID, user.get().getId());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));

    }

    @Test
    public void TestfindByUsername() {
        Optional<User> user = userDao.findByUsername(EMAIL);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(EMAIL, user.get().getEmail());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));

    }

}