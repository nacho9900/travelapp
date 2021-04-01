package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.dto.authentication.AuthDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenUtil
{
    private PrivateKey key;

    private static final int ONE_MINUTE = 60;
    private static final int ONE_HOUR = 60 * ONE_MINUTE;
    private static final int ONE_DAY = 24 * ONE_HOUR;
    private static final int TEN_DAYS = 10 * ONE_DAY;

    public JwtTokenUtil( InputStream secretKey ) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance( "RSA" );
            byte[] keyArray = new byte[secretKey.available()];
            secretKey.read( keyArray );
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec( keyArray );
            key = keyFactory.generatePrivate( spec );
        }
        catch ( InvalidKeySpecException | NoSuchAlgorithmException | IOException ex ) {
            ex.printStackTrace();
        }
    }

    public AuthDto create( UserDetails user ) {
        Claims claims = Jwts.claims()
                            .setSubject( user.getUsername() );

        LocalDateTime expiresIn = LocalDateTime.now().plusSeconds( ONE_DAY );

        claims.setExpiration( java.util.Date.from( expiresIn.atZone( ZoneId.systemDefault() ).toInstant() ) );

        return new AuthDto( Jwts.builder()
                                .setClaims( claims )
                                .signWith( key )
                                .compact(), expiresIn );
    }

    public Optional<String> getUserName( String token ) {
        try {
            JwtParser parser = Jwts.parserBuilder()
                                   .setSigningKey( key )
                                   .build();
            Claims claims = (Claims) parser.parse( token )
                                           .getBody();

            return Optional.of( claims.getSubject() );
        }
        catch ( JwtException | ClassCastException ex ) {
            return Optional.empty();
        }
    }

    public boolean isExpired( String token ) {
        try {
            JwtParser parser = Jwts.parserBuilder()
                                   .setSigningKey( key )
                                   .build();
            Claims claims = (Claims) parser.parse( token )
                                           .getBody();

            return claims.getExpiration()
                         .before( Date.from( Instant.now() ) );
        }
        catch ( JwtException | ClassCastException ex ) {
            return true;
        }
    }
}
