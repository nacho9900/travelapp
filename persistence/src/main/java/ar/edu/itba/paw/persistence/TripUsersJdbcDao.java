package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripUsersDao;
import ar.edu.itba.paw.model.TripUser;
import ar.edu.itba.paw.model.UserRole;
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
public class TripUsersJdbcDao implements TripUsersDao {

    private final SimpleJdbcInsert jdbcInsert;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TripUsersJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("trip_users")
                .usingGeneratedKeyColumns("id");
    }

    private final static RowMapper<TripUser> ROW_MAPPER = (rs, rowNum) -> new TripUser(rs.getLong("id"),
            rs.getLong("trip_id"), rs.getLong("user_id"), UserRole.valueOf(rs.getString("user_role")));

    @Override
    public TripUser create(long tripId, long userId, UserRole userRole) {
        final Map<String, Object> args = new HashMap<>();
        args.put("trip_id", tripId);
        args.put("user_id", userId);
        args.put("user_role", userRole.toString());
        final Number tripUserId = jdbcInsert.executeAndReturnKey(args);
        return new TripUser(tripUserId.longValue(), tripId, userId, userRole);
    }

    @Override
    public Optional<TripUser> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM trip_users WHERE id = ?", ROW_MAPPER, id).stream().findFirst();
    }

    @Override
    public List<TripUser> findByTripId(long tripId) {
        return jdbcTemplate.query("SELECT * FROM trip_users WHERE trip_id = ?", ROW_MAPPER, tripId);
    }

    @Override
    public List<TripUser> findByUserId(long userId) {
        return jdbcTemplate.query("SELECT * FROM trip_users WHERE user_id = ?", ROW_MAPPER, userId);
    }
}
