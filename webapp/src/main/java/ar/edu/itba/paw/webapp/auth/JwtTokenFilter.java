package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.User;
import org.jose4j.jwt.JwtClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
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
import java.util.List;

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

        if ( isEmpty( header ) || !header.startsWith( "Bearer " ) )
        {
            filterChain.doFilter( request, response );
            return;
        }

        // Get jwt token and validate
        final String token = header.split( " " )[1].trim();

        JwtClaims claims;

        try
        {
            claims = jwtTokenUtil.validate( token );

            if ( claims == null )
            {
                filterChain.doFilter( request, response );
                return;
            }
        }
        catch ( Exception ex )
        {
            return;
        }

        // Get user identity and set it on the spring security context
        UserDetails userDetails = userDetailsService.loadUserByUsername( claims.getClaimValueAsString( "email" ) );

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( userDetails, null,
                userDetails == null ? new LinkedList<>() : userDetails.getAuthorities() );

        authentication.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );

        SecurityContextHolder.getContext()
                             .setAuthentication( authentication );

        filterChain.doFilter( request, response );
    }
}
