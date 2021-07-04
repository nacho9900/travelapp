package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.MailingService;
import ar.edu.itba.paw.interfaces.TripDao;
import ar.edu.itba.paw.interfaces.TripMemberService;
import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.PaginatedResult;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.TripMemberRole;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exception.CannotDeleteOwnerException;
import ar.edu.itba.paw.model.exception.EntityNotFoundException;
import ar.edu.itba.paw.model.exception.InvalidDateRangeException;
import ar.edu.itba.paw.model.exception.InvalidUserException;
import ar.edu.itba.paw.model.exception.UserNotMemberException;
import ar.edu.itba.paw.model.exception.UserNotOwnerOrAdminException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional
public class TripServiceImpl implements TripService
{
    @Autowired
    private TripDao tripDao;

    @Autowired
    private TripMemberService tripMemberService;

    @Autowired
    private UserService userService;

    @Autowired
    private MailingService mailingService;

    @Override
    public Trip create( String ownerUserName, String name, String description, LocalDate startDate, LocalDate endDate )
            throws InvalidUserException, InvalidDateRangeException {
        Optional<User> maybeOwner = userService.findByUsername( ownerUserName );

        if ( !maybeOwner.isPresent() || !maybeOwner.get().isVerified() ) {
            throw new InvalidUserException();
        }

        if ( startDate.isAfter( endDate ) ) {
            throw new InvalidDateRangeException();
        }

        User owner = maybeOwner.get();

        Trip trip = tripDao.create( name, description, startDate, endDate );
        TripMember member = tripMemberService.createOwner( trip, owner );
        trip.getMembers().add( member );

        return trip;
    }

    @Override
    public Optional<Trip> findById( long id ) {
        return tripDao.findById( id );
    }

    @Override
    public PaginatedResult<Trip> getAllUserTrips( String username, int page, int perPage )
            throws EntityNotFoundException {
        Optional<User> maybeUser = userService.findByUsername( username );

        if ( !maybeUser.isPresent() ) {
            throw new EntityNotFoundException();
        }

        return tripDao.findUserTrips( maybeUser.get().getId(), page, perPage );
    }

    @Override
    public Trip update( long id, String name, String description, LocalDate startDate, LocalDate endDate,
                        String editorUsername )
            throws EntityNotFoundException, UserNotOwnerOrAdminException, InvalidDateRangeException {
        Optional<Trip> maybeTrip = findById( id );

        if ( !maybeTrip.isPresent() ) {
            throw new EntityNotFoundException();
        }

        Trip trip = maybeTrip.get();

        if ( !tripMemberService.isUserOwnerOrAdmin( id, editorUsername ) ) {
            throw new UserNotOwnerOrAdminException();
        }

        if ( startDate.isAfter( endDate ) ) {
            throw new InvalidDateRangeException();
        }

        trip.setName( name );
        trip.setDescription( description );
        trip.setStartDate( startDate );
        trip.setEndDate( endDate );

        return tripDao.update( trip );
    }

    @Override
    public PaginatedResult<Trip> search( Double latitude, Double longitude, LocalDate from, LocalDate to, int page,
                                         int perPage ) {
        return tripDao.search( latitude, longitude, from, to, page, perPage );
    }

    @Override
    public void deleteMember( long id, long memberId, String username, Locale locale )
            throws EntityNotFoundException, UserNotOwnerOrAdminException, CannotDeleteOwnerException,
            UserNotMemberException {
        Optional<Trip> maybeTrip = findById( id );
        Optional<TripMember> maybeMember = tripMemberService.findById( memberId );

        if ( !maybeTrip.isPresent() || !maybeMember.isPresent() ) {
            throw new EntityNotFoundException();
        }

        if ( maybeMember.get().getRole().equals( TripMemberRole.OWNER ) ) {
            throw new CannotDeleteOwnerException();
        }

        if ( !tripMemberService.isUserOwnerOrAdmin( id, username ) ) {
            throw new UserNotOwnerOrAdminException();
        }

        tripMemberService.delete( memberId );

        Trip trip = maybeTrip.get();

        tripMemberService.getAllByTripId( id, username ).forEach( member -> {
            mailingService.exitTripEmail( member.getUser().getFullName(), member.getUser().getFullName(),
                                          member.getUser().getEmail(), id, trip.getName(), locale );
        } );
    }
}
