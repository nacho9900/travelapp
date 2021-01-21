package ar.edu.itba.paw.webapp.auth;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtTokenUtil
{
    private RsaJsonWebKey rsaJsonWebKey;

    private final JwtConsumer jwtConsumer;

    private final JsonWebSignature jsonWebSignature;

    public JwtTokenUtil() {
        try
        {
            this.rsaJsonWebKey = RsaJwkGenerator.generateJwk( 2048 );
            this.rsaJsonWebKey.setKeyId( "nacho2021" );
        }
        catch ( JoseException e )
        {
            e.printStackTrace();
        }

        this.jwtConsumer = new JwtConsumerBuilder().setExpectedIssuer( "PAW2019a4" )
                                                   .setRequireExpirationTime()
                                                   .setAllowedClockSkewInSeconds( 30 )
                                                   .setVerificationKey( rsaJsonWebKey.getKey() )
                                                   .setJweAlgorithmConstraints(
                                                           AlgorithmConstraints.ConstraintType.PERMIT,
                                                           AlgorithmIdentifiers.RSA_USING_SHA256 )
                                                   .build();

        jsonWebSignature = new JsonWebSignature();
        jsonWebSignature.setKey( rsaJsonWebKey.getRsaPrivateKey() );
        jsonWebSignature.setAlgorithmHeaderValue( AlgorithmIdentifiers.RSA_USING_SHA256 );
    }

    public String create( String email ) {
        JwtClaims claims = new JwtClaims();
        claims.setIssuer( "PAW2019a4" );
        claims.setExpirationTimeMinutesInTheFuture( 10 );
        claims.setIssuedAtToNow();
        claims.setClaim( "email", email );

        jsonWebSignature.setPayload( claims.toJson() );

        String jwt = "";

        try
        {
            jwt = jsonWebSignature.getCompactSerialization();
        }
        catch ( JoseException e )
        {
            //Do Nothing
        }

        return jwt;
    }

    public JwtClaims validate( String token ) throws InvalidJwtException {
        try
        {
            return jwtConsumer.processToClaims( token );
        }
        catch ( InvalidJwtException ex )
        {
            if ( ex.hasExpired() )
            {
                return null;
            }
            else
            {
                throw ex;
            }
        }
    }

    public long getExpiresIn( String token ) throws InvalidJwtException, MalformedClaimException {
        JwtClaims claims = validate( token );
        NumericDate expiresInNumericData = claims.getExpirationTime();

        return expiresInNumericData.getValue();
    }
}
