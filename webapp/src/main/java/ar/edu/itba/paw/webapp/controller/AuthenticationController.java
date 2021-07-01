package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.PasswordRecoveryTokenService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exception.EntityAlreadyExistsException;
import ar.edu.itba.paw.model.exception.InvalidTokenException;
import ar.edu.itba.paw.model.exception.InvalidUserException;
import ar.edu.itba.paw.model.exception.UserNotVerifiedException;
import ar.edu.itba.paw.webapp.dto.authentication.PasswordRecoveryDto;
import ar.edu.itba.paw.webapp.dto.authentication.SignUpDto;
import ar.edu.itba.paw.webapp.dto.authentication.TokenPasswordDto;
import ar.edu.itba.paw.webapp.dto.authentication.VerificationDto;
import ar.edu.itba.paw.webapp.dto.errors.ErrorDto;
import ar.edu.itba.paw.webapp.dto.errors.ErrorsDto;
import ar.edu.itba.paw.webapp.dto.users.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Optional;
import java.util.Set;

@Component
@Path( "/auth" )
public class AuthenticationController
{
    @Autowired
    private UserService userService;

    @Autowired
    private Validator validator;

    @Autowired
    private PasswordRecoveryTokenService passwordRecoveryTokenService;

    @Context
    private UriInfo uriInfo;

    @POST
    @Path( "/signup" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public Response signup( @RequestBody SignUpDto signUpDto, @Context HttpServletRequest httpRequest ) {
        Set<ConstraintViolation<SignUpDto>> violations = validator.validate( signUpDto );

        if ( !violations.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST ).build();
        }

        User user = null;

        try {
            user = userService.create( signUpDto.getFirstname(), signUpDto.getLastname(), signUpDto.getEmail(),
                                       signUpDto.getPassword(), signUpDto.getBirthday(), signUpDto.getNationality(),
                                       null, httpRequest.getLocale() );
        }
        catch ( EntityAlreadyExistsException e ) {
            return Response.status( Response.Status.CONFLICT ).build();
        }

        return Response.created( uriInfo.getBaseUriBuilder()
                                        .path( UsersController.class )
                                        .path( UsersController.class, "get" )
                                        .build( user.getId() ) ).entity( UserDto.fromUser( user, uriInfo ) ).build();
    }

    @POST
    @Path( "/password-recovery" )
    public Response passwordRecovery(
            @RequestBody PasswordRecoveryDto passwordRecoveryDto, @Context HttpServletRequest httpRequest ) {
        Set<ConstraintViolation<PasswordRecoveryDto>> violations = validator.validate( passwordRecoveryDto );

        if ( !violations.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( ErrorsDto.fromConstraintsViolations( violations ) )
                           .build();
        }

        try {
            userService.initPasswordRecovery( passwordRecoveryDto.getEmail(), httpRequest.getLocale() );
        }
        catch ( InvalidUserException e ) {
            return Response.ok().build();
        }

        return Response.ok().build();
    }

    @POST
    @Path( "/change-password" )
    public Response changePassword( @RequestBody TokenPasswordDto tokenPasswordDto ) {
        Set<ConstraintViolation<TokenPasswordDto>> violations = validator.validate( tokenPasswordDto );

        if ( !violations.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( ErrorsDto.fromConstraintsViolations( violations ) )
                           .build();
        }

        try {
            userService.changePassword( tokenPasswordDto.getToken(), tokenPasswordDto.getPassword() );
        }
        catch ( InvalidTokenException | UserNotVerifiedException e ) {
            return Response.status( Response.Status.NOT_FOUND ).build();
        }

        return Response.ok().build();
    }

    @POST
    @Path( "/verify" )
    public Response verify( @RequestBody VerificationDto verificationDto ) {
        if ( verificationDto.getToken() == null || !userService.verifyEmail( verificationDto.getToken() ) ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( new ErrorDto( "invalid token", "token" ) )
                           .build();
        }

        return Response.ok().build();
    }
}
