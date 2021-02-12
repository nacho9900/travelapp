package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.TripMember;

import java.util.List;
import java.util.Optional;

public interface TripMemberDao
{
    Optional<TripMember> findById(long id);
    List<TripMember> getAllByTripId(long tripId);
    boolean memberBelongsToTheTrip(long id, long tripId);
    Optional<TripMember> findByTripIdAndUsername( String username, long tripId );
    void delete(long id);
    TripMember update( TripMember tripMember );
}
