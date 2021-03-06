package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserPicturesService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserPicture;
import ar.edu.itba.paw.model.exception.EntityNotFoundException;
import ar.edu.itba.paw.model.exception.ImageFormatException;
import ar.edu.itba.paw.model.exception.ImageMaxSizeException;
import ar.edu.itba.paw.model.exception.UnauthorizedException;
import ar.edu.itba.paw.model.exception.ValidationException;
import ar.edu.itba.paw.webapp.dto.errors.ErrorDto;
import ar.edu.itba.paw.webapp.dto.errors.ErrorsDto;
import ar.edu.itba.paw.webapp.dto.general.FileDto;
import ar.edu.itba.paw.webapp.dto.users.NewPasswordDto;
import ar.edu.itba.paw.webapp.dto.users.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.Set;

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
    @Path( "/{id}" )
    public Response edit( @PathParam( "id" ) long id, @RequestBody UserDto userDto ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Set<ConstraintViolation<UserDto>> violations = validator.validate( userDto );

        if ( !violations.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( ErrorsDto.fromConstraintsViolations( violations ) )
                           .build();
        }

        Optional<User> maybeLoggedUser = userService.findByUsername( username );
        Optional<User> maybeUserToEdit = userService.findById( id );

        if ( !maybeLoggedUser.isPresent() || !maybeUserToEdit.isPresent() ||
             maybeLoggedUser.get().getId() != userDto.getId() ||
             maybeLoggedUser.get().getId() != maybeUserToEdit.get().getId() ) {
            return Response.status( Response.Status.UNAUTHORIZED ).build();
        }

        User userUpdates = userDto.toUser();

        User user = userService.update( maybeLoggedUser.get(), userUpdates.getFirstname(), userUpdates.getLastname(),
                                        userUpdates.getBirthday(), userUpdates.getNationality(),
                                        userUpdates.getBiography() );

        return Response.ok().entity( UserDto.fromUser( user, uriInfo ) ).build();
    }

    @GET
    @Path( "/{id}" )
    public Response get( @PathParam( "id" ) long id ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> maybeLoggedUser = userService.findByUsername( username );
        Optional<User> maybeRequestedUser = userService.findById( id );

        if ( !maybeLoggedUser.isPresent() || !maybeRequestedUser.isPresent() ||
             maybeRequestedUser.get().getId() != maybeLoggedUser.get().getId() ) {
            return Response.status( Response.Status.UNAUTHORIZED ).build();
        }

        return Response.ok().entity( maybeLoggedUser.map( x -> UserDto.fromUser( x, uriInfo ) ) ).build();
    }

    @POST
    @Path( "/{id}/change-password" )
    public Response changePassword( @PathParam( "id" ) long id, @RequestBody NewPasswordDto newPasswordDto ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Set<ConstraintViolation<NewPasswordDto>> violations = validator.validate( newPasswordDto );

        if ( !violations.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( ErrorsDto.fromConstraintsViolations( violations ) )
                           .build();
        }

        Optional<User> maybeUser = userService.findByUsername( username );
        Optional<User> maybeRequestedUser = userService.findById( id );

        if ( !maybeUser.isPresent() || !maybeRequestedUser.isPresent() ||
             maybeRequestedUser.get().getId() != maybeUser.get().getId() || !maybeUser.get().isVerified() ) {
            return Response.status( Response.Status.UNAUTHORIZED ).build();
        }

        if ( !userService.matchPassword( maybeUser.get().getPassword(), newPasswordDto.getPasswordCurrent() ) ) {
            return Response.status( Response.Status.CONFLICT )
                           .entity( new ErrorDto( "passwords didn't match" ) )
                           .build();
        }

        User user = maybeUser.get();

        try {
            userService.changePassword( user.getEmail(), newPasswordDto.getPasswordCurrent(),
                                        newPasswordDto.getPasswordNew() );
        }
        catch ( EntityNotFoundException e ) {
            //Do Nothing
        }
        catch ( ValidationException e ) {
            return Response.status( Response.Status.CONFLICT )
                           .entity( new ErrorDto( "passwords didn't match" ) )
                           .build();
        }

        return Response.ok().build();
    }

    //region picture

    @PUT
    @Path( "/{id}/picture" )
    public Response createOrUpdatePicture( @PathParam( "id" ) long id, @RequestBody FileDto fileDto ) {
        Set<ConstraintViolation<FileDto>> violations = validator.validate( fileDto );

        if ( !violations.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( ErrorsDto.fromConstraintsViolations( violations ) )
                           .build();
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            userPicturesService.createOrUpdate( fileDto.getFilename(), fileDto.getFileBase64(), username, id );
        }
        catch ( ImageMaxSizeException | ImageFormatException e ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( new ErrorDto( "invalid image format or size" ) )
                           .build();
        }
        catch ( UnauthorizedException | EntityNotFoundException e ) {
            return Response.status( Response.Status.UNAUTHORIZED ).build();

        }

        return Response.created( uriInfo.getAbsolutePathBuilder().build() ).build();
    }

    @GET
    @Path( "/{id}/picture" )
    @Produces( "image/png" )
    @Cacheable
    public Response getPicture(
            @PathParam( "id" ) long id,
            @QueryParam( "width" ) Integer width, @QueryParam( "height" ) Integer height, @Context Request request ) {
        Optional<UserPicture> maybeUserPicture = userPicturesService.findByUserId( id );

        if ( !maybeUserPicture.isPresent() ) {
            return Response.status( Response.Status.NOT_FOUND ).build();
        }

        UserPicture userPicture = maybeUserPicture.get();

        EntityTag etag;

        if ( width != null && height != null ) {
            etag = new EntityTag( Integer.toString( userPicture.hashCode() * width.hashCode() * height.hashCode() ) );
            userPicture.setPicture( userPicturesService.resize( userPicture.getPicture(), width, height ) );
        }
        else if ( width != null ) {
            etag = new EntityTag( Integer.toString( userPicture.hashCode() * width.hashCode() * 7 ) );
            userPicture.setPicture( userPicturesService.resizeWidth( userPicture.getPicture(), width ) );
        }
        else if ( height != null ) {
            etag = new EntityTag( Integer.toString( userPicture.hashCode() * height.hashCode() * 11 ) );
            userPicture.setPicture( userPicturesService.resizeHeight( userPicture.getPicture(), height ) );
        }
        else {
            etag = new EntityTag( Integer.toString( userPicture.hashCode() ) );
        }

        Response.ResponseBuilder builder = request.evaluatePreconditions( etag );

        CacheControl cc = new CacheControl();
        cc.setMaxAge( 3600 );

        if ( builder == null ) {
            return Response.ok( new ByteArrayInputStream( userPicture.getPicture() ) )
                           .tag( etag )
                           .cacheControl( cc )
                           .build();
        }
        else {
            return builder.cacheControl( cc ).build();
        }
    }
    //endregion
}
