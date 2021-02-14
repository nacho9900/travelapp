package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.TripPicturesDao;
import ar.edu.itba.paw.interfaces.TripPicturesService;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripPicture;
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
    TripPicturesDao tripPicturesDao;

    private final int MAX_PICTURE_SIZE = 5000000; //5MB

    @Override
    public TripPicture create( Trip trip, String name, byte[] image ) {
        if ( image.length > MAX_PICTURE_SIZE ) {
            return null;
        }

        return tripPicturesDao.create( trip, name, image );
    }

    @Override
    public TripPicture create( Trip trip, String name, String imageBase64 ) {
        try {
            byte[] image = Base64.getDecoder().decode( imageBase64 );

            if ( !isContentTypeImage( name ) ) {
                return null;
            }

            return create( trip, name, image );
        }
        catch ( IllegalArgumentException e ) {
            return null;
        }
    }


    @Override
    public TripPicture update( TripPicture tripPicture, Trip trip, String name, String imageBase64 ) {
        try {
            byte[] image = Base64.getDecoder().decode( imageBase64 );

            if ( !isContentTypeImage( name ) ) {
                return null;
            }

            return update( tripPicture, trip, name, image );
        }
        catch ( IllegalArgumentException e ) {
            return null;
        }
    }

    @Override
    public TripPicture update( TripPicture tripPicture, Trip trip, String name, byte[] image ) {
        if(!isContentTypeImage( name ) || image.length > MAX_PICTURE_SIZE) {
            return null;
        }

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

    private boolean isContentTypeImage( String name ) {
        String contentType = URLConnection.guessContentTypeFromName( name );

        return contentType.startsWith( "image/" );
    }
}
