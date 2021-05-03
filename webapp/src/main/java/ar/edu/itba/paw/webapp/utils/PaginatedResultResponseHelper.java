package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.model.PaginatedResult;
import org.glassfish.jersey.server.Uri;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.LinkedList;
import java.util.List;

public class PaginatedResultResponseHelper
{
    public static Response.ResponseBuilder makeResponseBuilder( PaginatedResult<?> paginatedResult, UriInfo uriInfo ) {
        return makeResponseBuilder( paginatedResult, uriInfo, new LinkedList<>() );
    }

    public static Response.ResponseBuilder makeResponseBuilder( PaginatedResult<?> paginatedResult, UriInfo uriInfo,
                                                                List<MyQueryParam> myQueryParams ) {
        if(paginatedResult.isEmpty()) {
            return Response.ok();
        }

        UriBuilder firstBuilder = uriInfo.getAbsolutePathBuilder()
                                         .queryParam( "page", 1 )
                                         .queryParam( "perPage", paginatedResult.getItemsPerPage() );
        myQueryParams.forEach( x -> firstBuilder.queryParam( x.getName(), x.getValue() ) );
        UriBuilder lastBuilder = uriInfo.getAbsolutePathBuilder()
                                        .queryParam( "page", paginatedResult.getLastPage() )
                                        .queryParam( "perPage", paginatedResult.getItemsPerPage() );
        myQueryParams.forEach( x -> lastBuilder.queryParam( x.getName(), x.getValue() ) );

        Response.ResponseBuilder responseBuilder = Response.ok()
                                                           .link( firstBuilder.build(), "first" )
                                                           .link( lastBuilder.build(), "last" );

        if ( paginatedResult.hasNextPage() ) {
            UriBuilder nextBuilder = uriInfo.getAbsolutePathBuilder()
                                            .queryParam( "page", paginatedResult.getPage() + 1 )
                                            .queryParam( "perPage", paginatedResult.getItemsPerPage() );
            myQueryParams.forEach( x -> nextBuilder.queryParam( x.getName(), x.getValue() ) );
            responseBuilder.link( nextBuilder.build(), "next" );
        }

        if ( paginatedResult.hasPreviousPage() ) {
            UriBuilder prevBuilder = uriInfo.getAbsolutePathBuilder()
                                            .queryParam( "page", paginatedResult.getPage() - 1 )
                                            .queryParam( "perPage", paginatedResult.getItemsPerPage() );
            myQueryParams.forEach( x -> prevBuilder.queryParam( x.getName(), x.getValue() ) );
            responseBuilder.link( prevBuilder.build(), "prev" );
        }

        return responseBuilder.header( "Access-Control-Expose-Headers", "link" );
    }
}
