package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;

import java.util.Optional;

public class UserServiceImpl implements UserService {

    @Override
    public Optional<User> findByid(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }
}
