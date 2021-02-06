package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.dto.authentication.AuthDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
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
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername( username );

            if ( passwordEncoder.matches( password, userDetails.getPassword() ) ) {
                return Optional.of( jwtTokenUtil.create( userDetails ) );
            }
        } catch ( UsernameNotFoundException ex ) {
            return Optional.empty();
        }

        return Optional.empty();
    }
}
