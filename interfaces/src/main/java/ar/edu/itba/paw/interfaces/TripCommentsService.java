package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.exception.UserNotMemberException;

import java.util.List;

public interface TripCommentsService
{
    TripComment create( long tripId, String username, String comment ) throws UserNotMemberException;

    List<TripComment> getAllByTripId( long tripId, String username ) throws UserNotMemberException;

    void deleteAllByMemberId( long memberId );
}
