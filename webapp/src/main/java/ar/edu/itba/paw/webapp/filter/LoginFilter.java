package ar.edu.itba.paw.webapp.filter;

import ar.edu.itba.paw.webapp.auth.JwtAuthenticationService;
import ar.edu.itba.paw.webapp.config.JacksonObjectMapperProvider;
import ar.edu.itba.paw.webapp.dto.authentication.AuthDto;
import ar.edu.itba.paw.webapp.exception.UserAuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter extends AbstractAuthenticationProcessingFilter
{
    private final JwtAuthenticationService jwtAuthenticationService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    public LoginFilter( String defaultFilterProcessesUrl, JwtAuthenticationService jwtAuthenticationService,
                        UserDetailsService userDetailsService, ObjectMapper objectMapper ) {
        super( new AntPathRequestMatcher( defaultFilterProcessesUrl ) );
        this.jwtAuthenticationService = jwtAuthenticationService;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain )
            throws IOException, ServletException {
        final String method = ( (HttpServletRequest) req ).getMethod();

        if ( method.equals( "OPTIONS" ) ) {
            HttpServletResponse response = (HttpServletResponse) res;
            response.setStatus( HttpServletResponse.SC_OK );
            setCORS( response );
            return;
        }

        super.doFilter( req, res, chain );
    }

    @Override
    public Authentication attemptAuthentication( HttpServletRequest request, HttpServletResponse response )
            throws AuthenticationException {
        Authentication authentication = jwtAuthenticationService.getLoginAuthenticationFromRequest( request,
                                                                                                    objectMapper );

        if ( authentication == null ) {
            setCORS( response );
            throw new UserAuthenticationException( "Authentication failed" );
        }

        return authentication;
    }

    @Override
    protected void successfulAuthentication( HttpServletRequest request, HttpServletResponse response,
                                             FilterChain chain, Authentication authResult )
            throws IOException {
        UserDetails userDetails = userDetailsService.loadUserByUsername( authResult.getName() );
        AuthDto authDto = jwtAuthenticationService.createAuthDto( userDetails );
        response.getWriter().write( objectMapper.writeValueAsString( authDto ) );
        response.setStatus( HttpServletResponse.SC_OK );
        response.setContentType( "application/json" );
        setCORS( response );
    }

    private void setCORS( HttpServletResponse response ) {
        response.setHeader( "Access-Control-Allow-Origin", "*" );
        response.setHeader( "Access-Control-Allow-Headers", "*" );
        response.setHeader( "Access-Control-Allow-Methods", "POST, OPTIONS" );
        response.setHeader( "Access-Control-Allow-Credentials", "*" );
    }
}
