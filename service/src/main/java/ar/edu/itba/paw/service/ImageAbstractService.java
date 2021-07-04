package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.exception.ImageFormatException;
import ar.edu.itba.paw.model.exception.ImageMaxSizeException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Base64;

public abstract class ImageAbstractService
{
    private final Base64.Decoder decoder = Base64.getDecoder();
    protected final int MAX_PICTURE_SIZE = 5000000; //5MB

    protected byte[] decode( String name, String imageBase64 ) throws ImageFormatException, ImageMaxSizeException {
        if ( !isContentTypeImage( name ) ) {
            throw new ImageFormatException();
        }

        byte[] image;

        try {
            image = decoder.decode( imageBase64 );
        }
        catch ( IllegalArgumentException e ) {
            throw new ImageFormatException();
        }

        if ( image.length > MAX_PICTURE_SIZE ) {
            throw new ImageMaxSizeException();
        }

        return image;
    }

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

    protected boolean isContentTypeImage( String name ) {
        String contentType = URLConnection.guessContentTypeFromName( name );

        return contentType != null && contentType.startsWith( "image/" );
    }
}
