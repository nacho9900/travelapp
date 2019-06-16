package ar.edu.itba.paw.interfaces;


import ar.edu.itba.paw.model.Trip;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TripDao {

    public Trip create(long userId, long startPlaceId, String name, String description, LocalDate startDate, LocalDate endDate);
    public Optional<Trip> findById(long id);
    public List<Trip> findByName(String name);
    public List<Trip> getAllTrips(int pageNum);
    public List<Trip> findUserCreatedTrips(long userId, int pageNum);
    public void deleteTrip(long tripId);
    public int countAllTrips();

}
