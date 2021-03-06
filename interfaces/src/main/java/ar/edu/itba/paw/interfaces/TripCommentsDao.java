package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.TripMember;

import java.util.List;
import java.util.Optional;

public interface TripCommentsDao
{
    TripComment create( TripMember member, String comment );

    Optional<TripComment> findById( long id );

    void deleteAllByMemberId( long memberId );

    List<TripComment> getAllByTripId( long tripId );
}
