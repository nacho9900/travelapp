package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.UserPicturesDao;
import ar.edu.itba.paw.interfaces.UserPicturesService;
import ar.edu.itba.paw.model.UserPicture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserPicturesServiceImpl implements UserPicturesService {

    @Autowired
    private UserPicturesDao upd;

    @Override
    public UserPicture create(long userId, byte[] image) {
        return upd.create(userId, image);
    }

    @Override
    public Optional<UserPicture> findByUserId(long userId) {
        return upd.findByUserId(userId);
    }
}
