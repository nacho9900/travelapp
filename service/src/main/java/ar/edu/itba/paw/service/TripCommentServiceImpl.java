package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.TripCommentsDao;
import ar.edu.itba.paw.interfaces.TripCommentsService;
import ar.edu.itba.paw.interfaces.TripMemberService;
import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.exception.UserNotMemberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TripCommentServiceImpl implements TripCommentsService
{
    @Autowired
    private TripCommentsDao tripCommentsDao;

    @Autowired
    private TripMemberService tripMemberService;

    @Override
    public TripComment create( long tripId, String username, String comment ) throws UserNotMemberException {
        Optional<TripMember> maybeMember = tripMemberService.findByTripIdAndUsername( tripId, username, username );

        if ( !maybeMember.isPresent() ) {
            throw new UserNotMemberException();
        }

        return tripCommentsDao.create( maybeMember.get(), comment );
    }

    @Override
    public List<TripComment> getAllByTripId( long tripId, String username ) throws UserNotMemberException {
        if ( !tripMemberService.isUserMember( tripId, username ) ) {
            throw new UserNotMemberException();
        }

        return tripCommentsDao.getAllByTripId( tripId );
    }

    @Override
    public void deleteAllByMemberId( long memberId ) {
        tripCommentsDao.deleteAllByMemberId( memberId );
    }
}
