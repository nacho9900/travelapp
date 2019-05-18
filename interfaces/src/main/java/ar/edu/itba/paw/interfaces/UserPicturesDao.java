package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.UserPicture;

public interface UserPicturesDao {

    public UserPicture create(long userId, byte[] image);


}
