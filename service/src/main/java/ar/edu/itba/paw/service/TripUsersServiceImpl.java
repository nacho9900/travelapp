package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.TripUsersDao;
import ar.edu.itba.paw.interfaces.TripUsersService;
import ar.edu.itba.paw.model.TripUser;
import ar.edu.itba.paw.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TripUsersServiceImpl implements TripUsersService {

    @Autowired
    TripUsersDao tud;

    @Override
    public TripUser create(long tripId, long userId, UserRole userRole) {
        return tud.create(tripId, userId, userRole);
    }

    @Override
    public Optional<TripUser> findById(long id) {
        return tud.findById(id);
    }

    @Override
    public List<TripUser> findByTripId(long tripId) {
        return tud.findByTripId(tripId);
    }

    @Override
    public List<TripUser> findByUserId(long userId) {
        return tud.findByUserId(userId);
    }
}
