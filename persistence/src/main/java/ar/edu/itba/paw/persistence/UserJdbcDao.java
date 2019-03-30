package ar.edu.itba.paw.persistence;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserJdbcDao implements UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    private final static RowMapper<User> ROW_MAPPER = (rs, rowNum) -> null;

    @Override
    public Optional<User> findById(final long id) {
        final List<User> list = jdbcTemplate.query("SELECT * FROM users WHERE userid = ?",ROW_MAPPER, id);
        if (list.isEmpty()) {
            return null;
        }
        return Optional.of(list.get(0));
    }



    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }
}
