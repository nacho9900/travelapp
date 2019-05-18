package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.UserPicture;

public interface UserPicturesService {

    public UserPicture create(long userId, byte[] image);
}
