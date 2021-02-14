package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.dto.errors.ErrorDto;
import ar.edu.itba.paw.webapp.dto.errors.ErrorsDto;
import ar.edu.itba.paw.webapp.dto.users.NewPasswordDto;
import ar.edu.itba.paw.webapp.dto.users.UserDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
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
import java.util.Set;
import java.util.stream.Collectors;

@Component  //For autowired
@Path( "/users" )
public class UsersController
{
    private static final Integer PAGE_SIZE = 10;

    @Autowired
    private UserService userService;

    @Autowired
    private Validator validator;

    @Context
    private UriInfo uriInfo;

    //TODO: Paging
    @GET
    @Produces( value = {MediaType.APPLICATION_JSON} )
    public Response listUsers( @QueryParam( "page" ) @DefaultValue( "1" ) int page ) {
        final List<UserDto> users = userService.getAll( page, PAGE_SIZE ).stream().map( UserDto::fromUser ).collect(
                Collectors.toList() );

        return Response.ok( new GenericEntity<List<UserDto>>( users )
        {
        } ).link( uriInfo.getAbsolutePathBuilder().queryParam( "page", 1 ).build(), "first" ).link(
                uriInfo.getAbsolutePathBuilder().queryParam( "page", 5 ).build(), "last" ).link(
                uriInfo.getAbsolutePathBuilder().queryParam( "page", page + 1 ).build(), "next" ).link(
                uriInfo.getAbsolutePathBuilder().queryParam( "page", page - 1 ).build(), "prev" ).build();
    }

    @PUT
    @Path( "/current" )
    public Response edit( @RequestBody UserDto userDto ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Set<ConstraintViolation<UserDto>> violations = validator.validate( userDto );

        if ( !violations.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST ).entity(
                    ErrorsDto.fromConstraintsViolations( violations ) ).build();
        }

        Optional<User> maybeUser = userService.findByUsername( username );

        if ( !maybeUser.isPresent() || maybeUser.get().getId() != userDto.getId() ) {
            return Response.status( Response.Status.UNAUTHORIZED ).build();
        }

        User userUpdates = userDto.toUser();

        User user = userService.update( maybeUser.get(), userUpdates.getFirstname(), userUpdates.getLastname(),
                                        userUpdates.getBirthday(), userUpdates.getNationality(),
                                        userUpdates.getBiography() );

        return Response.ok().entity( UserDto.fromUser( user ) ).build();
    }

    @GET
    @Path( "/current" )
    public Response getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> maybeUser = userService.findByUsername( username );

        if ( !maybeUser.isPresent() ) {
            return Response.status( Response.Status.UNAUTHORIZED ).build();
        }

        return Response.ok().entity( maybeUser.map( UserDto::fromUser ) ).build();
    }

    @POST
    @Path( "/change-password" )
    public Response changePassword( @RequestBody NewPasswordDto newPasswordDto ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Set<ConstraintViolation<NewPasswordDto>> violations = validator.validate( newPasswordDto );

        if ( !violations.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST ).entity(
                    ErrorsDto.fromConstraintsViolations( violations ) ).build();
        }

        Optional<User> maybeUser = userService.findByUsername( username );

        if ( !maybeUser.isPresent() ) {
            return Response.status( Response.Status.UNAUTHORIZED ).build();
        }

        if ( !userService.matchPassword( maybeUser.get().getPassword(), newPasswordDto.getPasswordCurrent() ) ) {
            return Response.status( Response.Status.CONFLICT )
                           .entity( new ErrorDto( "passwords didn't match" ) )
                           .build();
        }

        User user = userService.changePassword( maybeUser.get(), newPasswordDto.getPasswordNew() );

        return Response.ok().build();
    }
}
