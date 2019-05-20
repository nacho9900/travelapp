package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripPicturesDao;
import ar.edu.itba.paw.model.TripPicture;
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
public class TripPicturesJdbcDao implements TripPicturesDao {

    private final SimpleJdbcInsert jdbcInsert;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TripPicturesJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("trip_pictures")
                .usingGeneratedKeyColumns("id");
    }

    private final static RowMapper<TripPicture> ROW_MAPPER = (rs, rowNum) -> new TripPicture(rs.getLong("id"),
            rs.getBytes("image"), rs.getLong("trip_id"));

    @Override
    public TripPicture create(long tripId, byte[] image) {
        final Map<String, Object> args = new HashMap<>();
        args.put("trip_id", tripId);
        args.put("image", image);
        final Number id = jdbcInsert.executeAndReturnKey(args);
        return new TripPicture(id.longValue(), image, tripId);
    }

    @Override
    public Optional<TripPicture> findByTripId(long tripId) {
        return jdbcTemplate.query("SELECT * FROM trip_pictures WHERE trip_id = ?", ROW_MAPPER, tripId).stream().findFirst();
    }

    @Override
    public boolean deleteByTripId(long tripId) {
        return jdbcTemplate.update("DELETE FROM trip_pictures WHERE trip_id = ?", tripId) != 0;
    }

}
