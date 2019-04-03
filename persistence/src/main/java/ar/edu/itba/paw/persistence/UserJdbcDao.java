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

    }

    private final static RowMapper<User> ROW_MAPPER = (rs, rowNum) -> new User(rs.getString("firstname"),
            rs.getString("lastname"), rs.getString("email"), rs.getString("password"),
            rs.getLong("id"));

    @Override
    public Optional<User> findById(final long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE id = ?", ROW_MAPPER, id).stream().findFirst();
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
        return jdbcTemplate.query("SELECT * FROM users WHERE email = ?",ROW_MAPPER,email).stream().findFirst();
    }
}
