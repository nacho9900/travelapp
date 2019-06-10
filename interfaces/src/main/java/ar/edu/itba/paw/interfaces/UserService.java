package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.DataPair;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserRole;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;


public interface UserService {

    public Optional<User> findByid(final long id);
    public Optional<User> findByUsername(final String email);
    public User create(final String firstname, final String lastname, final String email, final String password,
                       final Calendar birthday, final String nationality);

    public void persistTrip(User user, Trip trip);

}
