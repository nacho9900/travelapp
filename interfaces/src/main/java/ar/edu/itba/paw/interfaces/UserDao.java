package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserRole;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> findById(final long id);
    Optional<User> findByUsername(final String email);
    User create(final String firstname, final String lastname, final String email, final String password,
                Calendar birthday, final String nationality);
    void persistTrip(User user, Trip trip);
    boolean update(User u);
}
