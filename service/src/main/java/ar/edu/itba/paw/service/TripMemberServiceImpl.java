package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.TripCommentsService;
import ar.edu.itba.paw.interfaces.TripMemberDao;
import ar.edu.itba.paw.interfaces.TripMemberService;
import ar.edu.itba.paw.interfaces.TripRateService;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.TripMemberRole;
import ar.edu.itba.paw.model.User;
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
    public List<TripMember> getAllByTripId( long tripId ) {
        return tripMemberDao.getAllByTripId( tripId );
    }

    @Override
    public boolean memberBelongsToTheTrip( long id, long tripId ) {
        return tripMemberDao.memberBelongsToTheTrip( id, tripId );
    }

    @Override
    public Optional<TripMember> findByTripIdAndUsername( long tripId, String username ) {
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
}
