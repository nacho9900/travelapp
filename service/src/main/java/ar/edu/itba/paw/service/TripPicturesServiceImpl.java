package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.TripMemberService;
import ar.edu.itba.paw.interfaces.TripPicturesDao;
import ar.edu.itba.paw.interfaces.TripPicturesService;
import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripPicture;
import ar.edu.itba.paw.model.exception.EntityNotFoundException;
import ar.edu.itba.paw.model.exception.ImageFormatException;
import ar.edu.itba.paw.model.exception.ImageMaxSizeException;
import ar.edu.itba.paw.model.exception.UserNotOwnerOrAdminException;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.net.URLConnection;
import java.util.Base64;
import java.util.Optional;

@Service
@Transactional
public class TripPicturesServiceImpl extends ImageAbstractService implements TripPicturesService
{
    @Autowired
    private TripPicturesDao tripPicturesDao;

    @Autowired
    private TripMemberService tripMemberService;

    @Autowired
    private TripService tripService;

    private final int MAX_PICTURE_SIZE = 5000000; //5MB

    @Override
    public TripPicture createOrUpdate( long tripId, String name, String imageBase64, String username )
            throws ImageMaxSizeException, UserNotOwnerOrAdminException, EntityNotFoundException, ImageFormatException {
        Optional<Trip> maybeTrip = tripService.findById( tripId );

        if ( !maybeTrip.isPresent() ) {
            throw new EntityNotFoundException();
        }

        if ( !tripMemberService.isUserOwnerOrAdmin( tripId, username ) ) {
            throw new UserNotOwnerOrAdminException();
        }

        byte[] image = decode( name, imageBase64 );

        Optional<TripPicture> tripPicture = findByTripId( tripId );

        if ( tripPicture.isPresent() ) {
            return update( tripPicture.get(), maybeTrip.get(), name, image );
        }
        else {
            return create( maybeTrip.get(), name, image );
        }
    }

    protected TripPicture create( Trip trip, String name, byte[] image ) {
        return tripPicturesDao.create( trip, name, image );
    }

    protected TripPicture update( TripPicture tripPicture, Trip trip, String name, byte[] image ) {
        tripPicture.setName( name );
        tripPicture.setPicture( image );
        tripPicture.setTrip( trip );
        return tripPicturesDao.update( tripPicture );
    }

    @Override
    public Optional<TripPicture> findByTripId( long tripId ) {
        return tripPicturesDao.findByTripId( tripId );
    }

    @Override
    public byte[] resize( byte[] image, int width, int height ) {
        BufferedImage bufferedImage = createImageFromBytes( image );

        bufferedImage = Scalr.resize( bufferedImage, width, height );

        return imageToByteArray( bufferedImage, "png" );
    }

    @Override
    public byte[] resizeHeight( byte[] image, int height ) {
        return resizeOneDimension( image, height, Scalr.Mode.FIT_TO_HEIGHT );
    }

    @Override
    public byte[] resizeWidth( byte[] image, int width ) {
        return resizeOneDimension( image, width, Scalr.Mode.FIT_TO_WIDTH );
    }

    private byte[] resizeOneDimension( byte[] image, int size, Scalr.Mode mode ) {
        BufferedImage bufferedImage = createImageFromBytes( image );

        bufferedImage = Scalr.resize( bufferedImage, mode, size );

        return imageToByteArray( bufferedImage, "png" );
    }
}
