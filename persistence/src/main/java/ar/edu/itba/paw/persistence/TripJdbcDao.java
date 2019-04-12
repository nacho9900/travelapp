package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TripDao;
import ar.edu.itba.paw.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.Calendar;
import java.util.Optional;

public class TripJdbcDao implements TripDao {

    private final SimpleJdbcInsert jdbcInsert;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TripJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("trips")
                .usingGeneratedKeyColumns("id");

    }

    @Override
    public Trip create(long placeid, String name, String description, Calendar startDate, Calendar endDate) {
        return null;
    }

    @Override
    public Optional<Trip> findById(long id) {
        return Optional.empty();
    }
}
