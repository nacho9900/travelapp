package ar.edu.itba.paw.webapp.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class CORSResponseFilter implements ContainerResponseFilter
{
    private static final Logger LOGGER = LoggerFactory.getLogger( CORSResponseFilter.class );

    @Override
    public void filter( ContainerRequestContext requestContext, ContainerResponseContext responseContext ) {
        responseContext.getHeaders().add( "Access-Control-Allow-Origin", "*" );
        responseContext.getHeaders().add( "Access-Control-Allow-Headers", "*" );
        responseContext.getHeaders().add( "Access-Control-Allow-Methods", "*" );
        responseContext.getHeaders().add( "Access-Control-Allow-Credentials", "true" );
        LOGGER.debug( "CORS applied" );
    }
}
