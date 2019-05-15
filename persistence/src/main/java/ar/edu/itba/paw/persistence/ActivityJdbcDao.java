package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.interfaces.ActivityDao;
import ar.edu.itba.paw.model.Activity;
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
public class ActivityJdbcDao implements ActivityDao {

    private final SimpleJdbcInsert jdbcInsert;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ActivityJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("activities")
                .usingGeneratedKeyColumns("id");
    }

    private final static RowMapper<Activity> ROW_MAPPER = (rs, rowNum) -> new Activity(rs.getLong("id"), rs.getString("name"),
            rs.getLong("category_id"));

    private final static RowMapper<String> ROW_MAPPER_CAT = (rs, rowNum) -> rs.getString("name");

    @Override
    public Optional<Activity> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM activities WHERE id = ?", ROW_MAPPER, id).stream().findFirst();
    }

    @Override
    public Optional<Activity> findByName(String name) {
        return jdbcTemplate.query("SELECT * FROM activities WHERE name = ?", ROW_MAPPER, name).stream().findFirst();
    }

    @Override
    public Activity create(String name, long categoryId) {
        final Map<String, Object> args = new HashMap<>();
        args.put("name", name);
        args.put("category_id", categoryId);
        final Number activityId = jdbcInsert.executeAndReturnKey(args);
        return new Activity(activityId.longValue(), name, categoryId);
    }

    @Override
    public List<Activity> getTripActivities(long tripId) {
        return jdbcTemplate.query("SELECT * FROM activities, trip_activities WHERE trip_id = ? AND activities.id = activity_id ", ROW_MAPPER, tripId);
    }

    @Override
    public List<String> getActivityPlaces(long id) {
        return jdbcTemplate.query("SELECT places.name FROM activities,activity_places,places WHERE  activities.id = ? AND activity_id = activities.id AND place_id = places.id ", ROW_MAPPER_CAT, id);
    }

    @Override
    public List<String> getActivityCategories(long id) {
        return jdbcTemplate.query("SELECT name FROM activity_categories, activities WHERE activities.id = ? AND category_id = activity_categories.id", ROW_MAPPER_CAT, id);
    }
}
