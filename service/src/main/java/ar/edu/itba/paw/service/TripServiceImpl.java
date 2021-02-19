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

    @Override
    public Trip create( long userId, long startPlaceId, String name, String description, LocalDate startDate,
                        LocalDate endDate ) {
        return tripDao.create( userId, startPlaceId, name, description, startDate, endDate );
    }

    @Override
    public Trip create( Trip trip ) {
        if ( trip.getStartDate().isAfter( trip.getEndDate() ) ) {
            //trow ex
        }

        tripDao.create( trip );

        return trip;
    }

    @Override
    public Optional<Trip> findById( long id ) {
        return tripDao.findById( id );
    }

    @Override
    public List<Trip> findByName( String name ) {
        return tripDao.findByName( name );
    }

    @Override
    public List<Trip> getAllTrips( int pageNum ) {
        return tripDao.getAllTrips( pageNum );
    }

    @Override
    public List<Trip> getAllUserTrips( User user ) {
        return tripDao.findUserTrips( user.getId() );
    }

    @Override
    public int countAllTrips() {
        return tripDao.countAllTrips();
    }

    @Override
    public List<Trip> findByCategory( String category ) {
        return tripDao.findByCategory( category );
    }

    @Override
    public List<Trip> findByPlace( String placeName ) {
        return tripDao.findByPlace( placeName );
    }

    @Override
    public List<Trip> findWithFilters( Map<String, Object> filterMap ) {
        return tripDao.findWithFilters( filterMap );
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

        return tripDao.update(trip);
    }

    @Override
    public PaginatedResult<Trip> search( String text, Double latitude, Double longitude, LocalDate from, LocalDate to, int page ) {
        return tripDao.searchByTextAndNear( text, latitude, longitude, from, to, page );
    }
}
