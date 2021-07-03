package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.User;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface TripMemberService
{
    Optional<TripMember> findById( long id );

    List<TripMember> getAllByTripId( long tripId );

    boolean isUserMember( long id, long tripId );

    Optional<TripMember> findByTripIdAndUsername( long tripId, String username );

    void delete( Trip trip, long id, Locale locale );

    TripMember update( TripMember tripMember );

    TripMember create( Trip trip, User user );

    TripMember createOwner( Trip trip, User user );

    List<TripMember> getAllAdmins( long tripId );

    boolean isUserOwnerOrAdmin( long tripId, String username );

    boolean isUserMember( long id, String username );
}
