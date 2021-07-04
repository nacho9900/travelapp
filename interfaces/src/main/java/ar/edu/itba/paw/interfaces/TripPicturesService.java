package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.TripPicture;
import ar.edu.itba.paw.model.exception.EntityNotFoundException;
import ar.edu.itba.paw.model.exception.ImageFormatException;
import ar.edu.itba.paw.model.exception.ImageMaxSizeException;
import ar.edu.itba.paw.model.exception.UserNotOwnerOrAdminException;

import java.util.Optional;

public interface TripPicturesService
{
    TripPicture createOrUpdate( long tripId, String name, String imageBase64, String username )
            throws ImageMaxSizeException, UserNotOwnerOrAdminException, EntityNotFoundException, ImageFormatException;

    Optional<TripPicture> findByTripId( long tripId );

    byte[] resize( byte[] image, int width, int height );

    byte[] resizeHeight( byte[] image, int height );

    byte[] resizeWidth( byte[] image, int width );
}
