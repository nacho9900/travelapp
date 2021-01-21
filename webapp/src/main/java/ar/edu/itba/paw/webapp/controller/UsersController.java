package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.auth.JwtTokenUtil;
import ar.edu.itba.paw.webapp.auth.LoginHelper;
import ar.edu.itba.paw.webapp.dto.AuthDto;
import ar.edu.itba.paw.webapp.dto.AuthRequestDto;
import ar.edu.itba.paw.webapp.dto.UserDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
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
    private UserService userService;

    @Autowired
    private LoginHelper loginHelper;

    @Context
    private UriInfo uriInfo;

    //TODO: Paging
    @GET
    @Produces( value = {MediaType.APPLICATION_JSON} )
    public Response listUsers( @QueryParam( "page" ) @DefaultValue( "1" ) int page ) {
        final List<UserDto> users = userService.getAll( page, PAGE_SIZE )
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

    @POST
    @Path( "/login" )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response login( @RequestBody AuthRequestDto authRequestDto ) {
        String email = authRequestDto.getEmail();
        String password = authRequestDto.getPassword();

        Optional<AuthDto> maybeAuth = loginHelper.authenticate( email, password );

        if ( maybeAuth.isPresent() )
        {
            return Response.ok( maybeAuth.get() )
                           .build();
        }
        else {
            return Response.status( Response.Status.UNAUTHORIZED).build();
        }
    }

    @PUT
    @Consumes( MediaType.APPLICATION_JSON )
    public Response edit( @RequestBody UserDto userDto ) {
        return Response.ok( userDto )
                       .build();
    }
}
