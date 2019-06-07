package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserRole;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public interface UserDao {

    public Optional<User> findById(final long id);
    public Optional<User> findByUsername(final String email);
    public User create(final String firstname, final String lastname, final String email, final String password,
                       Calendar birthday, final String nationality);

    public Optional<UserRole> getUserRole(long userId, long tripId);

}
