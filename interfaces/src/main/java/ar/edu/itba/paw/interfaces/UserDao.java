package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> findById(final long id);
    Optional<User> findByUsername(String username);

}
