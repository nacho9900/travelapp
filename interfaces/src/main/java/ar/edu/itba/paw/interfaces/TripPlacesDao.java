package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.TripPlace;

import java.util.List;
import java.util.Optional;

public interface TripPlacesDao {

    public TripPlace create(long tripId, long placeId);
    public Optional<TripPlace> findById(long id);
    public List<TripPlace> findByTripId(long tripId);
    public List<TripPlace> findByPlaceId(long placeId);
}
