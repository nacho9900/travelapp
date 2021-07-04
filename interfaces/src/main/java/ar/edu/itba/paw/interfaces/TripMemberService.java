package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exception.UserNotMemberException;

import java.util.List;
import java.util.Optional;

public interface TripMemberService
{
    Optional<TripMember> findById( long id );

    List<TripMember> getAllByTripId( long tripId, String memberUsername ) throws UserNotMemberException;

    boolean isUserMember( long id, long tripId );

    Optional<TripMember> findByTripIdAndUsername( long tripId, String username, String memberUsername )
            throws UserNotMemberException;

    void delete( long id );

    TripMember update( TripMember tripMember );

    TripMember create( Trip trip, User user );

    TripMember createOwner( Trip trip, User user );

    List<TripMember> getAllAdmins( long tripId );

    boolean isUserOwnerOrAdmin( long tripId, String username );

    boolean isUserMember( long id, String username );
}
