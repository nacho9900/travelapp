package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripActivitiesDao;
import ar.edu.itba.paw.model.TripActivity;
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
public class TripActivitiesJdbcDao implements TripActivitiesDao {

    private final SimpleJdbcInsert jdbcInsert;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TripActivitiesJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("trip_activities")
                .usingGeneratedKeyColumns("id");
    }

    private final static RowMapper<TripActivity> ROW_MAPPER = (rs, rowNum) -> new TripActivity(rs.getLong("id"),
            rs.getLong("trip_id"), rs.getLong("place_id"), rs.getLong("activity_id"));

    @Override
    public TripActivity create(long tripId, long placeId, long activityId) {
        final Map<String, Object> args = new HashMap<>();
        args.put("trip_id", tripId);
        args.put("place_id", placeId);
        args.put("activity_id", activityId);
        final Number id = jdbcInsert.executeAndReturnKey(args);
        return new TripActivity(id.longValue(), tripId, placeId, activityId);
    }

    @Override
    public Optional<TripActivity> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM trip_activities WHERE id = ?", ROW_MAPPER, id).stream().findFirst();
    }
}
