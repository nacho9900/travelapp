package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PlaceDao;
import ar.edu.itba.paw.model.Place;
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
public class PlaceJdbcDao implements PlaceDao {

    private final SimpleJdbcInsert jdbcInsert;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PlaceJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("places")
                .usingGeneratedKeyColumns("id");

    }

    private final static RowMapper<Place> ROW_MAPPER = (rs, rowNum) -> new Place(rs.getLong("id"),
            rs.getString("google_id"), rs.getString("name"), rs.getDouble("latitude"),
            rs.getDouble("longitude"), rs.getString("address"));


    @Override
    public Place create(String googleId, String name, double latitude, double longitude, String address) {
        final Map<String, Object> args = new HashMap<>();
        args.put("google_id", googleId);
        args.put("name", name);
        args.put("latitude", latitude);
        args.put("longitude", longitude);
        args.put("address", address);
        final Number placeId = jdbcInsert.executeAndReturnKey(args);
        return new Place(placeId.longValue(), googleId, name, latitude, longitude, address);
    }

    @Override
    public Optional<Place> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM places WHERE id = ?", ROW_MAPPER, id).stream().findFirst();
    }

    @Override
    public Optional<Place> findByGoogleId(String googleId) {
        return jdbcTemplate.query("SELECT * FROM places WHERE google_id = ?", ROW_MAPPER, googleId).stream().findFirst();
    }


}
