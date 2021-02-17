package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripJoinRequest;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.User;

import java.util.List;
import java.util.Optional;

public interface TripJoinRequestService
{
    TripMember accept( TripJoinRequest tripJoinRequest );

    TripJoinRequest reject( TripJoinRequest tripJoinRequest );

    Optional<TripJoinRequest> findById( long id );

    TripJoinRequest create( User user, Trip trip, String message );

    Optional<TripJoinRequest> getLastByTripIdAndUsername( long tripId, String username );

    List<TripJoinRequest> getAllByTripId( long tripId );

    List<TripJoinRequest> getAllPendingByTripId( long tripId );

    List<TripJoinRequest> getAllAcceptedByTripId( long tripId );

    List<TripJoinRequest> getAllRejectedByTripId( long tripId );
}
