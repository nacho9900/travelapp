package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.TripPlacesDao;
import ar.edu.itba.paw.interfaces.TripPlacesService;
import ar.edu.itba.paw.model.DateManipulation;
import ar.edu.itba.paw.model.TripPlace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TripPlacesServiceImpl implements TripPlacesService {



    @Autowired
    TripPlacesDao tpd;

    @Override
    public TripPlace create(long tripId, long placeId) {
        return tpd.create(tripId, placeId);
    }

    @Override
    public Optional<TripPlace> findById(long id) {
        return tpd.findById(id);
    }

    @Override
    public List<TripPlace> findByTripId(long tripId) {
        return tpd.findByTripId(tripId);
    }

    @Override
    public List<TripPlace> findByPlaceId(long placeId) {
        return tpd.findByPlaceId(placeId);
    }
}
