package ar.edu.itba.paw.persistence;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;


import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserJdbcDao implements UserDao {

    private final SimpleJdbcInsert jdbcInsert;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users ("
                        + "id SERIAL PRIMARY KEY,"
                        + "firstname varchar(100),"
                        + "lastname varchar(100),"
                        + "email varchar(100),"
                        + "password varchar(100)"
                        + ")");


    }

    private final static RowMapper<User> ROW_MAPPER = (rs, rowNum) -> new User(rs.getString("firstname"),
            rs.getString("lastname"), rs.getString("email"), rs.getString("password"),
            rs.getLong("id"));

    @Override
    public Optional<User> findById(final long id) {
        final List<User> list = jdbcTemplate.query("SELECT * FROM users WHERE userid = ?",ROW_MAPPER, id);
        return Optional.of(list.get(0));
    }


    @Override
    public User create(final String firstname, final String lastname,  final String email, final String password) {
        final Map<String, Object> args = new HashMap<>();
        args.put("firstname", firstname);
        args.put("lastname", lastname);
        args.put("email", email);
        args.put("password", password);
        final Number userId = jdbcInsert.executeAndReturnKey(args);
        return new User(firstname, lastname, email, password, userId.longValue());
    }

    @Override
    public Optional<User> findByUsername(String email) {
        final List<User> list = jdbcTemplate.query("SELECT * FROM users WHERE email = ?",ROW_MAPPER, email);
        return list.stream().findAny();
    }
}
