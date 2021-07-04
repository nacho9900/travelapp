package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.MailingService;
import ar.edu.itba.paw.interfaces.TripJoinRequestDao;
import ar.edu.itba.paw.interfaces.TripJoinRequestService;
import ar.edu.itba.paw.interfaces.TripMemberService;
import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripJoinRequest;
import ar.edu.itba.paw.model.TripJoinRequestStatus;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exception.EntityNotFoundException;
import ar.edu.itba.paw.model.exception.UserAlreadyAMemberException;
import ar.edu.itba.paw.model.exception.UserAlreadyHaveAPendingRequestException;
import ar.edu.itba.paw.model.exception.UserNotMemberException;
import ar.edu.itba.paw.model.exception.UserNotOwnerOrAdminException;
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

    @Autowired
    private UserService userService;

    @Autowired
    private TripService tripService;

    @Override
    public TripMember accept( long id, String adminUsername, Locale locale )
            throws EntityNotFoundException, UserNotOwnerOrAdminException, UserNotMemberException {
        TripJoinRequest request = updateStatus( id, adminUsername, TripJoinRequestStatus.ACCEPTED );

        Trip trip = request.getTrip();

        TripMember member = tripMemberService.create( request.getTrip(), request.getUser() );

        User user = request.getUser();

        mailingService.requestAcceptedEmail( user.getFullName(), user.getEmail(), trip.getId(), trip.getName(),
                                             locale );

        tripMemberService.getAllByTripId( trip.getId(), adminUsername ).forEach( x -> {
            if ( x.getId() != member.getId() ) {
                mailingService.newMemberEmail( user.getFullName(), x.getUser().getFullName(), x.getUser().getEmail(),
                                               trip.getId(), trip.getName(), locale );
            }
        } );

        return member;
    }

    @Override
    public TripJoinRequest reject( long id, String adminUsername )
            throws EntityNotFoundException, UserNotOwnerOrAdminException {
        return updateStatus( id, adminUsername, TripJoinRequestStatus.REJECTED );
    }

    private TripJoinRequest updateStatus( long id, String adminUsername, TripJoinRequestStatus status )
            throws EntityNotFoundException, UserNotOwnerOrAdminException {
        Optional<TripJoinRequest> maybeRequest = findById( id );

        if ( !maybeRequest.isPresent() ) {
            throw new EntityNotFoundException();
        }

        TripJoinRequest request = maybeRequest.get();

        if ( !tripMemberService.isUserOwnerOrAdmin( request.getTrip().getId(), adminUsername ) ) {
            throw new UserNotOwnerOrAdminException();
        }

        request.setStatus( status );
        return tripJoinRequestDao.update( request );
    }

    @Override
    public TripJoinRequest create( String username, long tripId, String message, Locale locale )
            throws EntityNotFoundException, UserAlreadyAMemberException, UserAlreadyHaveAPendingRequestException {
        Optional<User> maybeUser = userService.findByUsername( username );
        Optional<Trip> maybeTrip = tripService.findById( tripId );

        if ( !maybeUser.isPresent() || !maybeTrip.isPresent() ) {
            throw new EntityNotFoundException();
        }

        User user = maybeUser.get();
        Trip trip = maybeTrip.get();

        if ( tripMemberService.isUserMember( tripId, username ) ) {
            throw new UserAlreadyAMemberException();
        }

        Optional<TripJoinRequest> lastRequest = getLastByTripIdAndUsername( tripId, username );

        if ( lastRequest.isPresent() && lastRequest.get().getStatus().equals( TripJoinRequestStatus.PENDING ) ) {
            throw new UserAlreadyHaveAPendingRequestException();
        }

        TripJoinRequest request = tripJoinRequestDao.create( user, trip, message );

        tripMemberService.getAllAdmins( tripId )
                         .forEach( x -> mailingService.sendNewJoinRequestEmail( user.getFullName(),
                                                                                x.getUser().getFullName(),
                                                                                x.getUser().getEmail(), tripId,
                                                                                locale ) );

        return request;
    }

    @Override
    public Optional<TripJoinRequest> findById( long id ) {
        return tripJoinRequestDao.findById( id );
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
}
