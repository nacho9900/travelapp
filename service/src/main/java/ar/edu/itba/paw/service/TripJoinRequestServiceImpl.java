package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.MailingService;
import ar.edu.itba.paw.interfaces.TripJoinRequestDao;
import ar.edu.itba.paw.interfaces.TripJoinRequestService;
import ar.edu.itba.paw.interfaces.TripMemberService;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripJoinRequest;
import ar.edu.itba.paw.model.TripJoinRequestStatus;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Transactional
@Service
public class TripJoinRequestServiceImpl implements TripJoinRequestService
{
    @Autowired
    private TripJoinRequestDao tripJoinRequestDao;

    @Autowired
    private TripMemberService tripMemberService;

    @Autowired
    private MailingService mailingService;

    @Override
    public TripMember accept( Trip trip, TripJoinRequest tripJoinRequest, Locale locale ) {
        User user = tripJoinRequest.getUser();

        TripMember member = tripMemberService.create( tripJoinRequest.getTrip(), tripJoinRequest.getUser() );

        updateStatus( tripJoinRequest, TripJoinRequestStatus.ACCEPTED );

        mailingService.requestAcceptedEmail( user.getFullName(), user.getEmail(), trip.getId(), trip.getName(),
                                             locale );

        tripMemberService.getAllByTripId( trip.getId() ).forEach( x -> {
            if ( x.getId() != member.getId() ) {
                mailingService.newMemberEmail( user.getFullName(), x.getUser().getFullName(), x.getUser().getEmail(),
                                               trip.getId(), trip.getName(), locale );
            }
        } );

        return member;
    }

    @Override
    public TripJoinRequest reject( TripJoinRequest tripJoinRequest ) {
        return updateStatus( tripJoinRequest, TripJoinRequestStatus.REJECTED );
    }

    private TripJoinRequest updateStatus( TripJoinRequest tripJoinRequest, TripJoinRequestStatus status ) {
        tripJoinRequest.setStatus( status );
        return tripJoinRequestDao.update( tripJoinRequest );
    }

    @Override
    public Optional<TripJoinRequest> findById( long id ) {
        return tripJoinRequestDao.findById( id );
    }

    @Override
    public TripJoinRequest create( User user, Trip trip, String message, Locale locale ) {
        TripJoinRequest request = tripJoinRequestDao.create( user, trip, message );

        tripMemberService.getAllAdmins( trip.getId() )
                         .forEach( x -> mailingService.sendNewJoinRequestEmail( user.getFullName(),
                                                                                x.getUser().getFullName(),
                                                                                x.getUser().getEmail(), trip.getId(),
                                                                                locale ) );

        return request;
    }

    @Override
    public Optional<TripJoinRequest> getLastByTripIdAndUsername( long tripId, String username ) {
        return tripJoinRequestDao.getLastByTripIdAndUsername( tripId, username );
    }

    @Override
    public List<TripJoinRequest> getAllByTripId( long tripId ) {
        return tripJoinRequestDao.getAllByTripId( tripId );
    }

    @Override
    public List<TripJoinRequest> getAllPendingByTripId( long tripId ) {
        return tripJoinRequestDao.getAllByTripIdAndStatus( tripId, TripJoinRequestStatus.PENDING );
    }

    @Override
    public List<TripJoinRequest> getAllAcceptedByTripId( long tripId ) {
        return tripJoinRequestDao.getAllByTripIdAndStatus( tripId, TripJoinRequestStatus.ACCEPTED );
    }

    @Override
    public List<TripJoinRequest> getAllRejectedByTripId( long tripId ) {
        return tripJoinRequestDao.getAllByTripIdAndStatus( tripId, TripJoinRequestStatus.REJECTED );
    }
}
