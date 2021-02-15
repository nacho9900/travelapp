package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripJoinRequest;
import ar.edu.itba.paw.model.TripJoinRequestStatus;
import ar.edu.itba.paw.model.User;

import java.util.List;
import java.util.Optional;

public interface TripJoinRequestDao
{
    Optional<TripJoinRequest> findById( long id );

    List<TripJoinRequest> getAllByTripId( long tripId );

    List<TripJoinRequest> getAllByTripIdAndStatus( long tripId, TripJoinRequestStatus status );

    Optional<TripJoinRequest> getLastByTripIdAndUsername( long tripId, String username );

    TripJoinRequest update( TripJoinRequest tripJoinRequest );

    TripJoinRequest create( User user, Trip trip, String message );
}
