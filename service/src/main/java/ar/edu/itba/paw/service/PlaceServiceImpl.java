package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.PlaceDao;
import ar.edu.itba.paw.interfaces.PlaceService;
import ar.edu.itba.paw.model.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceServiceImpl implements PlaceService {

    @Autowired
    PlaceDao pd;

    @Override
    public Place create(String googleId, String name, double latitude, double longitude, String address) {
        return pd.create(googleId,name,latitude,longitude,address);
    }

    @Override
    public Optional<Place> findById(long id) {
        return pd.findById(id);
    }

    @Override
    public Optional<Place> findByGoogleId(String googleId) {
        return pd.findByGoogleId(googleId);
    }

    @Override
    public List<Place> getTripPlaces(long tripId) {
        return pd.getTripPlaces(tripId);
    }
}
