package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;

import ar.edu.itba.paw.webapp.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component  //For autowired
@Path( "/users" )
public class UsersController
{
    private static final Integer PAGE_SIZE = 10;

    @Autowired
    private UserService us;

    @Context
    private UriInfo uriInfo;

    //TODO: Paging
    @GET
    @Produces( value = {MediaType.APPLICATION_JSON} )
    public Response listUsers( @QueryParam( "page" ) @DefaultValue( "1" ) int page )
    {
        final List<UserDto> users = us.getAll( page, PAGE_SIZE )
                .stream()
                .map( x -> UserDto.fromUser( x, uriInfo ) )
                .collect( Collectors.toList() );

        return Response.ok( new GenericEntity<List<UserDto>>( users )
        {
        } )
                .link( uriInfo.getAbsolutePathBuilder()
                        .queryParam( "page", 1 )
                        .build(), "first" )
                .link( uriInfo.getAbsolutePathBuilder()
                        .queryParam( "page", 5 )
                        .build(), "last" )
                .link( uriInfo.getAbsolutePathBuilder()
                        .queryParam( "page", page + 1 )
                        .build(), "next" )
                .link( uriInfo.getAbsolutePathBuilder()
                        .queryParam( "page", page - 1 )
                        .build(), "prev" )
                .build();
    }
}
