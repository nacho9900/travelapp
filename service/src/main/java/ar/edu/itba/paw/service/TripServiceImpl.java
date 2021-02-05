package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TripServiceImpl implements TripService
{

    @Autowired
    private TripDao td;

    @Autowired
    private ActivityDao ad;

    @Autowired
    private TripCommentsDao tcd;

    @Autowired
    private TripPicturesDao tpd;

    @Autowired
    private UserDao ud;

    @Override
    public Trip create( long userId, long startPlaceId, String name, String description, LocalDate startDate,
                        LocalDate endDate ) {
        return td.create( userId, startPlaceId, name, description, startDate, endDate );
    }

    @Override
    public Trip create( Trip trip ) {
        if(trip.getStartDate().isAfter( trip.getEndDate() )) {
            //trow ex
        }

        td.create( trip );

        return trip;
    }

    @Override
    public Optional<Trip> findById( long id ) {
        return td.findById( id );
    }

    @Override
    public List<Trip> findByName( String name ) {
        return td.findByName( name );
    }

    @Override
    public List<Trip> getAllTrips( int pageNum ) {
        return td.getAllTrips( pageNum );
    }

    //TODO: Arregar esto, ahora un miembre tiene el viaje
    @Override
    public List<Trip> getAllUserTrips( User user ) {
        List<Trip> trips = user.getMembers()
                               .stream()
                               .map( TripMember::getTrip )
                               .collect( Collectors.toList() ); //TODO: Optimizar esto
        List<Trip> createdTrips = td.findUserCreatedTrips( user.getId() );
        trips.addAll( createdTrips );
        Collections.sort( trips );
        return trips;
    }

    @Override
    public int countAllTrips() {
        return td.countAllTrips();
    }

    @Override
    public List<Trip> findByCategory( String category ) {
        return td.findByCategory( category );
    }

    @Override
    public List<Trip> findByPlace( String placeName ) {
        return td.findByPlace( placeName );
    }

    @Override
    public List<Trip> findWithFilters( Map<String, Object> filterMap ) {
        return td.findWithFilters( filterMap );
    }

}
