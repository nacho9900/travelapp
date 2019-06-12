package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.TripPicturesDao;
import ar.edu.itba.paw.interfaces.TripPicturesService;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripPicture;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
@Transactional
public class TripPicturesServiceImpl implements TripPicturesService {

    @Autowired
    TripPicturesDao tpd;

    private static final int RESOLUTION = 500;

    @Override
    public TripPicture create(Trip trip, byte[] image) {

        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(image));
            if(img == null ) throw new IllegalArgumentException();
            img = Scalr.resize(img, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, RESOLUTION, RESOLUTION, Scalr.OP_ANTIALIAS);
            ByteArrayOutputStream convertedImage = new ByteArrayOutputStream();
            ImageIO.write(img, "png", convertedImage);
            img.flush();
            return tpd.create(trip, convertedImage.toByteArray());
        }
        catch (IllegalArgumentException | IOException ex) {
            //no resizing
            return tpd.create(trip, image);
        }
    }

    @Override
    public Optional<TripPicture> findByTripId(long tripId) {
        return tpd.findByTripId(tripId);
    }

    @Override
    public boolean deleteByTripId(long tripId) {
        return tpd.deleteByTripId(tripId);
    }
}
