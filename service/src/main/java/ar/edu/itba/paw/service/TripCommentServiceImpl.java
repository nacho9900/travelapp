package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.TripCommentsDao;
import ar.edu.itba.paw.interfaces.TripCommentsService;
import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.TripMember;
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

    @Override
    public TripComment create( TripMember member, String comment ) {
        return tripCommentsDao.create( member, comment );
    }

    @Override
    public Optional<TripComment> getById( long id ) {
        return tripCommentsDao.findById( id );
    }

    @Override
    public List<TripComment> getAllByTripId( long tripId ) {
        return tripCommentsDao.getAllByTripId( tripId );
    }

    @Override
    public void deleteAllByMemberId(long memberId) {
        tripCommentsDao.deleteAllByMemberId( memberId );
    }
}
