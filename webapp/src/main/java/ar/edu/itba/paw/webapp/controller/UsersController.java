package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserPicturesService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.TripMemberRole;
import ar.edu.itba.paw.model.TripPicture;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserPicture;
import ar.edu.itba.paw.webapp.dto.errors.ErrorDto;
import ar.edu.itba.paw.webapp.dto.errors.ErrorsDto;
import ar.edu.itba.paw.webapp.dto.general.FileDto;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component  //For autowired
@Path( "/users" )
public class UsersController
{
    @Autowired
    private UserService userService;

    @Autowired
    private UserPicturesService userPicturesService;

    @Autowired
    private Validator validator;

    @Context
    private UriInfo uriInfo;

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

    //region picture

    @PUT
    @Path( "/current/picture" )
    public Response createOrUpdatePicture( @RequestBody FileDto fileDto ) {
        Set<ConstraintViolation<FileDto>> violations = validator.validate( fileDto );

        if ( !violations.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST ).entity(
                    ErrorsDto.fromConstraintsViolations( violations ) ).build();
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> maybeUser = userService.findByUsername( username );

        if ( !maybeUser.isPresent() ) {
            return Response.status( Response.Status.UNAUTHORIZED ).build();
        }

        User user = maybeUser.get();

        Optional<UserPicture> maybeUserPicture = userPicturesService.findByUserId( user.getId() );

        if ( maybeUserPicture.isPresent() ) {
            userPicturesService.update( maybeUserPicture.get(), user, fileDto.getFilename(), fileDto.getFileBase64() );
        }
        else {
            userPicturesService.create( user, fileDto.getFilename(), fileDto.getFileBase64() );
        }

        return Response.ok().build();
    }

    @GET
    @Path( "/{id}/picture" )
    @Produces( "image/png" )
    public Response getPicture(
            @PathParam( "id" ) long id, @QueryParam( "width" ) Integer width, @QueryParam( "height" ) Integer height ) {
        Optional<UserPicture> maybeUserPicture = userPicturesService.findByUserId( id );

        if ( !maybeUserPicture.isPresent() ) {
            return Response.status( Response.Status.NOT_FOUND ).build();
        }

        UserPicture userPicture = maybeUserPicture.get();

        if ( width != null && height != null ) {
            userPicture.setPicture( userPicturesService.resize( userPicture.getPicture(), width, height ) );
        }
        else if ( width != null ) {
            userPicture.setPicture( userPicturesService.resizeWidth( userPicture.getPicture(), width ) );
        }
        else if ( height != null ) {
            userPicture.setPicture( userPicturesService.resizeHeight( userPicture.getPicture(), height ) );
        }

        return Response.ok( new ByteArrayInputStream( userPicture.getPicture() ) ).build();
    }

    //endregion

}
