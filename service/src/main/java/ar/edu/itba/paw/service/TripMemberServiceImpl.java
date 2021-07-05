package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.TripCommentsService;
import ar.edu.itba.paw.interfaces.TripMemberDao;
import ar.edu.itba.paw.interfaces.TripMemberService;
import ar.edu.itba.paw.interfaces.TripRateService;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.TripMemberRole;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exception.UserNotMemberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class TripMemberServiceImpl implements TripMemberService
{
    @Autowired
    private TripMemberDao tripMemberDao;

    @Autowired
    private TripRateService tripRateService;

    @Autowired
    private TripCommentsService tripCommentsService;

    @Override
    public Optional<TripMember> findById( long id ) {
        return tripMemberDao.findById( id );
    }

    @Override
    public List<TripMember> getAllByTripId( long tripId, String memberUsername ) throws UserNotMemberException {
        if ( !isUserMember( tripId, memberUsername ) ) {
            throw new UserNotMemberException();
        }

        return tripMemberDao.getAllByTripId( tripId );
    }

    @Override
    public boolean isUserMember( long id, long tripId ) {
        return tripMemberDao.memberBelongsToTheTrip( id, tripId );
    }

    @Override
    public Optional<TripMember> findByTripIdAndUsername( long tripId, String username, String memberUsername )
            throws UserNotMemberException {
        if ( !isUserMember( tripId, memberUsername ) ) {
            throw new UserNotMemberException();
        }

        return tripMemberDao.findByTripIdAndUsername( username, tripId );
    }

    @Override
    public void delete( long id ) {
        tripRateService.deleteByMemberId( id );
        tripCommentsService.deleteAllByMemberId( id );
        tripMemberDao.delete( id );
    }

    @Override
    public TripMember update( TripMember tripMember ) {
        return tripMemberDao.update( tripMember );
    }

    @Override
    public TripMember create( Trip trip, User user ) {
        return tripMemberDao.create( trip, user, TripMemberRole.MEMBER, true );
    }

    @Override
    public TripMember createOwner( Trip trip, User user ) {
        return tripMemberDao.create( trip, user, TripMemberRole.OWNER, true );
    }

    @Override
    public List<TripMember> getAllAdmins( long tripId ) {
        return tripMemberDao.getAllAdmins( tripId );
    }

    @Override
    public boolean isUserOwnerOrAdmin( long tripId, String username ) {
        return tripMemberDao.isUserOwnerOrAdmin( tripId, username );
    }

    @Override
    public boolean isUserMember( long id, String username ) {
        return tripMemberDao.isUserMember( id, username );
    }
}
