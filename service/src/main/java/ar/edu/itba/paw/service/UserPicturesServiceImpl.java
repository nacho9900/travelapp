package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.UserPicturesDao;
import ar.edu.itba.paw.interfaces.UserPicturesService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserPicture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserPicturesServiceImpl implements UserPicturesService {

    @Autowired
    private UserPicturesDao upd;

    @Override
    public UserPicture create(User user, byte[] image) {
        return upd.create(user, image);
    }

    @Override
    public Optional<UserPicture> findByUserId(long userId) {
        return upd.findByUserId(userId);
    }

    @Override
    public boolean deleteByUserId(long userId) {
        return upd.deleteByUserId(userId);
    }
}
