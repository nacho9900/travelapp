package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.PlaceDao;
import ar.edu.itba.paw.interfaces.PlaceService;
import ar.edu.itba.paw.model.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PlaceServiceImpl implements PlaceService
{

    @Autowired
    private PlaceDao placeDao;

    @Override
    public Place createIfNotExists( String googleId, String name, double latitude, double longitude, String address ) {
        Optional<Place> maybePlace = findByGoogleId( googleId );
        return maybePlace.orElseGet( () -> placeDao.create( googleId, name, latitude, longitude, address ) );

    }

    @Override
    public Optional<Place> findById( long id ) {
        return placeDao.findById( id );
    }

    @Override
    public Optional<Place> findByGoogleId( String googleId ) {
        return placeDao.findByGoogleId( googleId );
    }
}
