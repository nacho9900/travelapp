package ar.edu.itba.paw.webapp.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;

import static org.springframework.util.StringUtils.isEmpty;

@Component
public class JwtTokenFilter extends OncePerRequestFilter
{
    @Autowired
    private TravelUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    protected JwtTokenFilter() {
        //
    }

    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response,
                                     FilterChain filterChain ) throws ServletException, IOException {
        response.setHeader( "Access-Control-Allow-Origin", "*" );
        response.setHeader( "Access-Control-Allow-Headers", "*" );
        response.setHeader( "Access-Control-Allow-Methods", "*" );
        response.setHeader( "Access-Control-Allow-Credentials", "true" );

        // Get authorization header and validate
        final String header = request.getHeader( HttpHeaders.AUTHORIZATION );

        if ( isEmpty( header ) || !header.startsWith( "Bearer " ) ) {
            filterChain.doFilter( request, response );
            return;
        }

        // Get jwt token and validate
        final String token = header.split( " " )[1].trim();

        Optional<String> maybeUsername = jwtTokenUtil.getUserName( token );

        if ( !maybeUsername.isPresent() ) {
            response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
            ( (HttpServletResponse) response ).sendError( HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token" );
            return;
        }

        if ( jwtTokenUtil.isExpired( token ) ) {
            response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
            ( (HttpServletResponse) response ).sendError( HttpServletResponse.SC_UNAUTHORIZED, "Token Expired" );
            return;
        }

        // Get user identity and set it on the spring security context
        UserDetails userDetails = userDetailsService.loadUserByUsername( maybeUsername.get() );

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( userDetails, null,
                userDetails == null ? new LinkedList<>() : userDetails.getAuthorities() );

        authentication.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );

        SecurityContextHolder.getContext()
                             .setAuthentication( authentication );

        filterChain.doFilter( request, response );
    }
}