package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.*;

import java.time.LocalDate;
import java.util.*;

public interface TripService {

    public Trip create(long createdBy, long place, String name, String description, LocalDate startDate, LocalDate endDate);
    public Trip create(Trip trip);
    public Optional<Trip> findById(long id);
    public List<Trip> getAllTrips(int pageNum);
    public List<Trip> findByName(String name);
    public List<Trip> getAllUserTrips(User user);
    public List<Trip> findByCategory(String category);
    public List<Trip> findByPlace(String placeName);
    public List<Trip> findWithFilters(Map<String,Object> filterMap);
    public int countAllTrips();
}
