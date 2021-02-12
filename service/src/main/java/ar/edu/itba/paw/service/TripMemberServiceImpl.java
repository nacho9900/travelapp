package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.TripMemberDao;
import ar.edu.itba.paw.interfaces.TripMemberService;
import ar.edu.itba.paw.model.TripMember;
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
    public Optional<TripMember> findByTripIdAndUsername( String username, long tripId ) {
        return tripMemberDao.findByTripIdAndUsername( username, tripId );
    }

    @Override
    public void delete( long id ) {
        tripMemberDao.delete( id );
    }

    @Override
    public TripMember update(TripMember tripMember) {
        return tripMemberDao.update( tripMember );
    }
}
