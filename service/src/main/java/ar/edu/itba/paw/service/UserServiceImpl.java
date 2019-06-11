package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findByid(long id) {
        return userDao.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String email) {
        return userDao.findByUsername(email);
    }


    @Override
    public User create(String firstname, String lastname, String email, String password, Calendar birthday, String nationality) {
        return userDao.create(firstname, lastname, email, passwordEncoder.encode(password), birthday, nationality);
    }

    @Override
    public boolean update(User user) {
        return userDao.update(user);
    }

    @Override
    public void persistTrip(User user, Trip trip) {
        userDao.persistTrip(user, trip);
    }

}
