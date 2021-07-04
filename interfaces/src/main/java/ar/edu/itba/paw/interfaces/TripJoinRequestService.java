package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.TripJoinRequest;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.exception.EntityNotFoundException;
import ar.edu.itba.paw.model.exception.UserAlreadyAMemberException;
import ar.edu.itba.paw.model.exception.UserAlreadyHaveAPendingRequestException;
import ar.edu.itba.paw.model.exception.UserNotMemberException;
import ar.edu.itba.paw.model.exception.UserNotOwnerOrAdminException;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface TripJoinRequestService
{
    TripMember accept( long id, String adminUsername, Locale locale )
            throws EntityNotFoundException, UserNotOwnerOrAdminException, UserNotMemberException;

    TripJoinRequest reject( long id, String adminUsername )
            throws EntityNotFoundException, UserNotOwnerOrAdminException;

    Optional<TripJoinRequest> findById( long id );

    TripJoinRequest create( String username, long tripId, String message, Locale locale )
            throws EntityNotFoundException, UserAlreadyAMemberException, UserAlreadyHaveAPendingRequestException;

    Optional<TripJoinRequest> getLastByTripIdAndUsername( long tripId, String username );

    List<TripJoinRequest> getAllByTripId( long tripId );

    List<TripJoinRequest> getAllPendingByTripId( long tripId );
}
