package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.TripUser;
import ar.edu.itba.paw.model.UserRole;

import java.util.List;
import java.util.Optional;

public interface TripUsersDao {

    public TripUser create(long tripId, long userId, UserRole userRole);
    public Optional<TripUser> findById(long id);
    public List<TripUser> findByTripId(long tripId);
    public List<TripUser> findByUserId(long userId);
}
