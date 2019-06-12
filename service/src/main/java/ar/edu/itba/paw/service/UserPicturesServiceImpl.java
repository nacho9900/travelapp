package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.UserPicturesDao;
import ar.edu.itba.paw.interfaces.UserPicturesService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserPicture;
import org.imgscalr.Scalr;
import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
@Transactional
public class UserPicturesServiceImpl implements UserPicturesService {

    @Autowired
    private UserPicturesDao upd;

    private static final int RESOLUTION = 200;

    @Override
    public UserPicture create(User user, byte[] image) {
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(image));
            if(img == null || img.getType() == BufferedImage.TYPE_CUSTOM)
                throw new IllegalArgumentException();
            img = Scalr.resize(img, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, RESOLUTION, RESOLUTION, Scalr.OP_ANTIALIAS);
            ByteArrayOutputStream convertedImage = new ByteArrayOutputStream();
            ImageIO.write(img, "png", convertedImage);
            img.flush();
            return upd.create(user, convertedImage.toByteArray());
        }
        catch (IllegalArgumentException|IOException ex) {
            //no resizing
            return upd.create(user, image);
        }
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
