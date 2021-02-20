package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.UserPicturesDao;
import ar.edu.itba.paw.interfaces.UserPicturesService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserPicture;
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
public class UserPicturesServiceImpl extends ImageAbstractService implements UserPicturesService
{

    @Autowired
    private UserPicturesDao userPicturesDao;

    private final int MAX_PICTURE_SIZE = 5000000; //5MB

    @Override
    public UserPicture create( User user, String name, byte[] image ) {
        if ( image.length > MAX_PICTURE_SIZE ) {
            return null;
        }

        return userPicturesDao.create( user, name, image );
    }

    @Override
    public UserPicture create( User user, String name, String imageBase64 ) {
        try {
            byte[] image = Base64.getDecoder().decode( imageBase64 );

            if ( !isContentTypeImage( name ) ) {
                return null;
            }

            return create( user, name, image );
        }
        catch ( IllegalArgumentException e ) {
            return null;
        }
    }

    @Override
    public UserPicture update( UserPicture userPicture, User user, String name, String imageBase64 ) {
        try {
            byte[] image = Base64.getDecoder().decode( imageBase64 );

            if ( !isContentTypeImage( name ) ) {
                return null;
            }

            return update( userPicture, user, name, image );
        }
        catch ( IllegalArgumentException e ) {
            return null;
        }
    }

    @Override
    public UserPicture update( UserPicture userPicture, User user, String name, byte[] image ) {
        if ( !isContentTypeImage( name ) || image.length > MAX_PICTURE_SIZE ) {
            return null;
        }

        userPicture.setName( name );
        userPicture.setPicture( image );
        userPicture.setUser( user );
        return userPicturesDao.update( userPicture );
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

    @Override
    public Optional<UserPicture> findByUserId( long userId ) {
        return userPicturesDao.findByUserId( userId );
    }

    @Override
    public boolean deleteByUserId( long userId ) {
        return userPicturesDao.deleteByUserId( userId );
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
