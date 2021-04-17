package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class TripServiceImpl implements TripService
{
    @Autowired
    private TripDao tripDao;

    @Autowired
    private TripMemberService tripMemberService;

    @Override
    public Trip create( User ownerUser, String name, String description, LocalDate startDate, LocalDate endDate ) {
        Trip trip = tripDao.create( name, description, startDate, endDate );
        tripMemberService.createOwner( trip, ownerUser );
        return trip;
    }

    @Override
    public Optional<Trip> findById( long id ) {
        return tripDao.findById( id );
    }

    @Override
    public PaginatedResult<Trip> getAllUserTrips( User user, int page ) {
        return tripDao.findUserTrips( user.getId(), page );
    }

    @Override
    public boolean isUserOwnerOrAdmin( long tripId, String username ) {
        return tripDao.isUserOwnerOrAdmin( tripId, username );
    }

    @Override
    public boolean isUserMember( long tripId, String username ) {
        return tripDao.isUserMember( tripId, username );
    }

    @Override
    public Trip update( Trip trip, String name, String description, LocalDate startDate, LocalDate endDate ) {
        trip.setName( name );
        trip.setDescription( description );
        trip.setStartDate( startDate );
        trip.setEndDate( endDate );

        return tripDao.update( trip );
    }

    @Override
    public PaginatedResult<Trip> search( String text, Double latitude, Double longitude, LocalDate from, LocalDate to
            , int page ) {
        return tripDao.search( text, latitude, longitude, from, to, page );
    }
}
