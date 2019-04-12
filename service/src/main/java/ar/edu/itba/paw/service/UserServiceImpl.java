package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

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
        return userDao.create(firstname, lastname, email, password, birthday, nationality);
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}
