package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.model.UserPicture;

import java.util.Optional;

public interface UserPicturesDao {

    public UserPicture create(long userId, byte[] image);

    public Optional<UserPicture> findByUserId(long userId);


}
