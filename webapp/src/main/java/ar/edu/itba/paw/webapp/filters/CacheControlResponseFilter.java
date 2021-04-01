package ar.edu.itba.paw.webapp.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;

@Component
public class CacheControlResponseFilter extends OncePerRequestFilter
{
    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain )
            throws IOException, ServletException {
        response.setHeader( HttpHeaders.CACHE_CONTROL, "public, max-age=31536000" );
        filterChain.doFilter( request, response );
    }
}
