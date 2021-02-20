package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.TripPicture;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class ImageAbstractService
{
    protected BufferedImage createImageFromBytes( byte[] imageData ) {
        ByteArrayInputStream bais = new ByteArrayInputStream( imageData );
        try {
            return ImageIO.read( bais );
        }
        catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    protected static byte[] imageToByteArray( BufferedImage bi, String format ) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( bi, format, baos );
            return baos.toByteArray();
        }
        catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }
}
