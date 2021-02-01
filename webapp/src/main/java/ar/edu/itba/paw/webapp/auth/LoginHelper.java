package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.dto.authentication.AuthDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoginHelper
{
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public Optional<AuthDto> authenticate( String username, String password ) {
        UserDetails userDetails = userDetailsService.loadUserByUsername( username );
        if ( passwordEncoder.matches( password, userDetails.getPassword() ) )
        {
            String token = jwtTokenUtil.create( username );
            try
            {
                long expiresIn = jwtTokenUtil.getExpiresIn(token);
                return Optional.of( new AuthDto( token, expiresIn) );

            }
            catch ( Exception e )
            {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }
}
