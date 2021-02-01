package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.TripRate;

import java.util.Optional;

public interface TripRateService {
    TripRate create( TripMember member, int rate);

    Optional<TripRate> findById(long id);

    void delete(long id);

    void update(TripRate id);

    Optional<TripRate> findByUserAndTrip(long tripId, long userId);
}
