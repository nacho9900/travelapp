package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.UserPicturesDao;
import ar.edu.itba.paw.interfaces.UserPicturesService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserPicture;
import ar.edu.itba.paw.model.exception.EntityNotFoundException;
import ar.edu.itba.paw.model.exception.ImageFormatException;
import ar.edu.itba.paw.model.exception.ImageMaxSizeException;
import ar.edu.itba.paw.model.exception.UnauthorizedException;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.util.Optional;

@Service
@Transactional
public class UserPicturesServiceImpl extends ImageAbstractService implements UserPicturesService
{
    @Autowired
    private UserPicturesDao userPicturesDao;

    @Autowired
    private UserService userService;

    private final int MAX_PICTURE_SIZE = 5000000; //5MB

    @Override
    public UserPicture createOrUpdate( String name, String imageBase64, String username, long userId )
            throws ImageMaxSizeException, ImageFormatException, UnauthorizedException, EntityNotFoundException {
        Optional<User> maybeUser = userService.findById( userId );

        if ( !maybeUser.isPresent() ) {
            throw new EntityNotFoundException();
        }

        User user = maybeUser.get();

        if ( !user.getEmail().equals( username ) ) {
            throw new UnauthorizedException();
        }

        byte[] image = decode( name, imageBase64 );

        Optional<UserPicture> maybePicture = findByUserId( userId );

        if ( maybePicture.isPresent() ) {
            return update( maybePicture.get(), user, name, image );
        }
        else {
            return create( user, name, image );
        }
    }

    public UserPicture create( User user, String name, byte[] image ) {
        return userPicturesDao.create( user, name, image );
    }

    public UserPicture update( UserPicture userPicture, User user, String name, byte[] image ) {
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

    private byte[] resizeOneDimension( byte[] image, int size, Scalr.Mode mode ) {
        BufferedImage bufferedImage = createImageFromBytes( image );

        bufferedImage = Scalr.resize( bufferedImage, mode, size );

        return imageToByteArray( bufferedImage, "png" );
    }
}
