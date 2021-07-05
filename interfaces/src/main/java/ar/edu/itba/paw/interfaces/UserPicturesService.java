package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.UserPicture;
import ar.edu.itba.paw.model.exception.EntityNotFoundException;
import ar.edu.itba.paw.model.exception.ImageFormatException;
import ar.edu.itba.paw.model.exception.ImageMaxSizeException;
import ar.edu.itba.paw.model.exception.UnauthorizedException;

import java.util.Optional;

public interface UserPicturesService
{
    UserPicture createOrUpdate( String name, String imageBase64, String username, long userId )
            throws ImageMaxSizeException, ImageFormatException, UnauthorizedException, EntityNotFoundException;

    byte[] resize( byte[] image, int width, int height );

    byte[] resizeHeight( byte[] image, int height );

    byte[] resizeWidth( byte[] image, int width );

    Optional<UserPicture> findByUserId( long userId );
}
