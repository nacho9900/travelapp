package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.*;

import java.time.LocalDate;
import java.util.*;

public interface TripService {
    Trip create( long createdBy, long place, String name, String description, LocalDate startDate, LocalDate endDate );
    Trip create( Trip trip );
    Optional<Trip> findById( long id );
    List<Trip> getAllTrips( int pageNum );
    List<Trip> findByName( String name );
    List<Trip> getAllUserTrips( User user );
    List<Trip> findByCategory( String category );
    List<Trip> findByPlace( String placeName );
    List<Trip> findWithFilters( Map<String, Object> filterMap );
    int countAllTrips();
    boolean isUserOwnerOrAdmin( long tripId, String username );
    boolean isUserMember(long tripId, String username);
    Trip update( Trip trip, String name, String description, LocalDate startDate, LocalDate endDate );
}
