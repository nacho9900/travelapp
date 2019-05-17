package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripDao;
import ar.edu.itba.paw.model.DateManipulation;
import ar.edu.itba.paw.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class TripJdbcDao implements TripDao {

    private final SimpleJdbcInsert jdbcInsert;

    private JdbcTemplate jdbcTemplate;

    private final static RowMapper<Trip> ROW_MAPPER = (rs, rowNum) -> new Trip(rs.getLong("id"),
            rs.getLong("startplace_id"),
            rs.getString("name"), rs.getString("description"),
            DateManipulation.dateToCalendar(rs.getDate("start_date")), DateManipulation
            .dateToCalendar(rs.getDate("end_date")));

    private final static RowMapper<String> ROW_MAPPER_ROLE = (rs, rowNum) -> rs.getString("user_role");

    private final static RowMapper<Integer> ROW_MAPPER_COUNT = (rs, rowNum) -> rs.getInt("qty");
    private final static int TRIPS_PER_PAGE = 4;


    @Autowired
    public TripJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("trips")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Trip create(long startPlaceId, String name, String description, Calendar startDate, Calendar endDate) {
        final Map<String, Object> args = new HashMap<>();
        args.put("startplace_id", startPlaceId);
        args.put("name", name);
        args.put("description",description);
        args.put("start_date",startDate.getTime());
        args.put("end_date", endDate.getTime());
        final Number tripid = jdbcInsert.executeAndReturnKey(args);
        return new Trip(tripid.longValue(), startPlaceId, name, description, startDate, endDate);
    }

    @Override
    public Optional<Trip> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM trips WHERE id = ?", ROW_MAPPER, id).stream().findFirst();
    }

    @Override
    public List<Trip> findUserTrips(long userId, int pageNum) {
        int offset = (pageNum - 1) * TRIPS_PER_PAGE;
        return jdbcTemplate.query("SELECT trips.* FROM trips, trip_users WHERE user_id = ? AND trip_id = trips.id OFFSET ? LIMIT 4", ROW_MAPPER, userId, offset);
    }

    @Override
    public boolean userIsAdmin(long userId, long tripId) {
        Optional<String> roleOpt = jdbcTemplate.query("SELECT user_role FROM trip_users WHERE user_id = ? AND trip_id = ?", ROW_MAPPER_ROLE, userId, tripId).stream().findFirst();
        return roleOpt.map(s -> s.equals("ADMIN")).orElse(false);
    }

    @Override
    public boolean isTravelling(long userId, long tripId) {
        Optional<Integer> roleOpt = jdbcTemplate.query("SELECT COUNT(*) AS qty FROM trip_users WHERE user_id = ? AND trip_id = ?", ROW_MAPPER_COUNT, userId, tripId).stream().findFirst();
        return roleOpt.filter(integer -> integer != 0).isPresent();
    }

    @Override
    public List<Trip> getAllTrips() {
        return jdbcTemplate.query("SELECT * FROM trips", ROW_MAPPER);
    }

    @Override
    public int countUserTrips(long userId) {
        Optional<Integer> qty = jdbcTemplate.query("SELECT COUNT(trips.id) AS qty FROM trips, trip_users WHERE user_id = ? AND trip_id = trips.id", ROW_MAPPER_COUNT, userId).stream().findFirst();
        return qty.orElse(0);
    }

    @Override
    public List<Trip> findByName(String name) {
        String likeName = "%" + name + "%";
        return jdbcTemplate.query("SELECT * FROM trips WHERE name ILIKE ?", ROW_MAPPER, likeName);
    }

}
