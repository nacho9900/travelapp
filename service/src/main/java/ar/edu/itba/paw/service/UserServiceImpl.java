package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.DataPair;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
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


    //TODO VER
    @Override
    public List<DataPair<User, UserRole>> getTripUsersAndRoles(Trip trip) {
        List<User> tripUsers = trip.getUsers();
        List<DataPair<User, UserRole>> pairList = new LinkedList<>();
        for(User user : tripUsers) {
            Optional<UserRole> role = userDao.getUserRole(user.getId(), trip.getId());
            pairList.add(new DataPair<>(user, role.get()));
        }
        return pairList;
    }

    @Override
    public Optional<UserRole> getUserRole(long userId, long tripId) {
        return userDao.getUserRole(userId, tripId);
    }


}
