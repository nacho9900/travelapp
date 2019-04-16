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

    private final static RowMapper<Trip> ROW_MAPPER = (rs, rowNum) -> new Trip(rs.getLong("startplace_id"),
            rs.getLong("id"), rs.getString("name"), rs.getString("description"),
            DateManipulation.dateToCalendar(rs.getDate("start_date")), DateManipulation.dateToCalendar(
                    rs.getDate("end_date")));

    @Autowired
    public TripJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("trips")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Trip create(long startplaceId, long id, String name, String description, Calendar startDate, Calendar endDate) {
        final Map<String, Object> args = new HashMap<>();
        args.put("startplace_id", startplaceId);
        args.put("name", name);
        args.put("description",description);
        args.put("start_date",startDate);
        args.put("end_date", endDate);
        final Number tripid = jdbcInsert.executeAndReturnKey(args);
        return new Trip(startplaceId, tripid.longValue(), name, description, startDate, endDate);
    }

    @Override
    public Optional<Trip> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM trips WHERE id = ?", ROW_MAPPER, id).stream().findFirst();
    }


    //
    @Override
    public List<Trip> findUserTrips(long userid) {
        return jdbcTemplate.query("SELECT trips.id, name, description , start_date, end_date, startplace_id" +
                " FROM trips, trip_users " +
                "WHERE user_id = ? AND trip_id = trips.id", ROW_MAPPER, userid);
    }


}
