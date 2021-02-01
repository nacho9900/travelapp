package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.TripMember;

import java.util.Optional;

public interface TripCommentsService
{
    public TripComment create( TripMember member, String comment );

    Optional<TripComment> getById( long id );
}
