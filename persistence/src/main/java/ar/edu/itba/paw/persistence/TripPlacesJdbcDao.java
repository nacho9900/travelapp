package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripPlacesDao;
import ar.edu.itba.paw.model.TripPlace;
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
public class TripPlacesJdbcDao implements TripPlacesDao {

    private final SimpleJdbcInsert jdbcInsert;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TripPlacesJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("trip_places")
                .usingGeneratedKeyColumns("id");
    }

    private final static RowMapper<TripPlace> ROW_MAPPER = (rs, rowNum) -> new TripPlace(rs.getLong("id"),
            rs.getLong("trip_id"), rs.getLong("place_id"));


    @Override
    public TripPlace create(long tripId, long placeId) {
        final Map<String, Object> args = new HashMap<>();
        args.put("trip_id", tripId);
        args.put("place_id", placeId);
        final Number tripPlaceId = jdbcInsert.executeAndReturnKey(args);
        return new TripPlace(tripPlaceId.longValue(), tripId, placeId);
    }

    @Override
    public Optional<TripPlace> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM trip_places WHERE id = ?", ROW_MAPPER, id).stream().findFirst();
    }

    @Override
    public List<TripPlace> findByTripId(long tripId) {
        return jdbcTemplate.query("SELECT * FROM trip_places WHERE trip_id = ?", ROW_MAPPER, tripId);
    }

    @Override
    public List<TripPlace> findByPlaceId(long placeId) {
        return jdbcTemplate.query("SELECT * FROM trip_places WHERE place_id = ?", ROW_MAPPER, placeId);
    }
}
