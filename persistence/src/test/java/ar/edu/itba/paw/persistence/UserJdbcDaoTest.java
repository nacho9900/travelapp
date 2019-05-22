package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserRole;
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
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Transactional
@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserJdbcDaoTest {
    private static final String PASSWORD = "password2";
    private static final String LASTNAME = "lastname2";
    private static final String FIRSTNAME = "firstname2";

    private static final String EMAIL = "fgorostiaga@itba.edu.ar";
    private static final String EMAIL2 = "test@mail.com2";
    private static final String NATIONALITY = "Argentina";
    private final Calendar birthday = initiateDate();

    private static final long tripId = 2;
    private static final long userId = 1;
    private static final long userId2 = 2;

    public Calendar initiateDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1997,6,16);
        return calendar;
    }

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
        final User user = userDao.create(FIRSTNAME, LASTNAME, EMAIL2, PASSWORD, birthday, NATIONALITY);
        Assert.assertNotNull(user);
        Assert.assertEquals(userId2, user.getId());
        Assert.assertEquals(LASTNAME, user.getLastname());
        JdbcTestUtils.countRowsInTableWhere(jdbcTemplate,"users","firstname = " + "'" + FIRSTNAME + "'");
    }

    @Test
    public void TestFindById() {
        Optional<User> user = userDao.findById(userId);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(userId, user.get().getId());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));

    }

    @Test
    public void TestFindByUsername() {
        Optional<User> user = userDao.findByUsername(EMAIL);
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals(EMAIL, user.get().getEmail());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));

    }


    @Test
    public void TestGetTripUsers() {
        List<User> users = userDao.getTripUsers(tripId);
        Assert.assertNotNull(users);
        Assert.assertTrue(!users.isEmpty());
        Assert.assertEquals(users.get(0).getId(), userId);
    }

    @Test
    public void TestGetUserRole() {
        Optional<UserRole> userRoleOptional = userDao.getUserRole(userId, tripId);
        Assert.assertNotNull(userRoleOptional);
        Assert.assertTrue(userRoleOptional.isPresent());
        Assert.assertEquals(userRoleOptional.get(), UserRole.ADMIN);
    }



}