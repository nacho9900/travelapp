package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserPicture;

import java.util.Optional;

public interface UserPicturesService
{
    UserPicture create( User user, String name, byte[] image );

    UserPicture create( User user, String name, String imageBase64 );

    UserPicture update( UserPicture userPicture, User user, String name, String imageBase64 );

    UserPicture update( UserPicture userPicture, User user, String name, byte[] image );

    byte[] resize( byte[] image, int width, int height );

    byte[] resizeHeight( byte[] image, int height );

    byte[] resizeWidth( byte[] image, int width );

    Optional<UserPicture> findByUserId( long userId );

    boolean deleteByUserId( long userId );
}
