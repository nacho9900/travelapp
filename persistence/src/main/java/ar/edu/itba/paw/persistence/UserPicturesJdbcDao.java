package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserPicturesDao;
import ar.edu.itba.paw.model.UserPicture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserPicturesJdbcDao implements UserPicturesDao {

    private final SimpleJdbcInsert jdbcInsert;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserPicturesJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("user_pictures")
                .usingGeneratedKeyColumns("id");
    }

    private final static RowMapper<UserPicture> ROW_MAPPER = (rs, rowNum) -> new UserPicture(rs.getLong("id"),
            rs.getBytes("profile_picture"), rs.getLong("user_id"));

    @Override
    public UserPicture create(long userId, byte[] image) {
        final Map<String, Object> args = new HashMap<>();
        args.put("profile_picture", image);
        args.put("user_id", userId);
        final Number id = jdbcInsert.executeAndReturnKey(args);
        return new UserPicture(id.longValue(), image, userId);
    }
}
