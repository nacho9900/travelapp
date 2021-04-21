package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.MailingService;
import ar.edu.itba.paw.interfaces.PasswordRecoveryTokenService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.PasswordRecoveryToken;
import ar.edu.itba.paw.model.User;
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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.Set;

@Component
@Path( "/auth" )
public class AuthenticationController extends BaseController
{
    @Autowired
    private UserService userService;

    @Autowired
    private Validator validator;

    @Autowired
    private MailingService mailingService;

    @Autowired
    private PasswordRecoveryTokenService passwordRecoveryTokenService;

    @Context
    private UriInfo uriInfo;

    @POST
    @Path( "/signup" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public Response signup( @RequestBody SignUpDto signUpDto ) {
        Set<ConstraintViolation<SignUpDto>> violations = validator.validate( signUpDto );

        if ( !violations.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST ).build();
        }

        Optional<User> maybeUser = this.userService.findByUsername( signUpDto.getEmail() );

        if ( maybeUser.isPresent() ) {
            return Response.status( Response.Status.CONFLICT ).build();
        }

        User user = userService.create( signUpDto.getFirstname(), signUpDto.getLastname(), signUpDto.getEmail(),
                                        signUpDto.getPassword(), signUpDto.getBirthday(), signUpDto.getNationality(),
                                        null );

        mailingService.welcomeAndVerificationEmail( user.getFullName(), user.getEmail(),
                                                    user.getVerificationToken().toString(), getFrontendUrl() );


        return Response.created( uriInfo.getBaseUriBuilder()
                                        .path( UsersController.class)
                                        .path( Long.toString( user.getId() ) )
                                        .build() ).entity( UserDto.fromUser( user ) ).build();
    }

    @POST
    @Path( "/password-recovery" )
    public Response passwordRecovery( @RequestBody PasswordRecoveryDto passwordRecoveryDto ) {
        Set<ConstraintViolation<PasswordRecoveryDto>> violations = validator.validate( passwordRecoveryDto );

        if ( !violations.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( ErrorsDto.fromConstraintsViolations( violations ) )
                           .build();
        }

        Optional<User> maybeUser = userService.findByUsername( passwordRecoveryDto.getEmail() );

        if ( !maybeUser.isPresent() || !maybeUser.get().isVerified() ) {
            return Response.ok().build();
        }

        User user = maybeUser.get();

        PasswordRecoveryToken token = passwordRecoveryTokenService.createOrUpdate( user );

        mailingService.sendPasswordRecoveryEmail( user.getFirstname() + "" + user.getLastname(), user.getEmail(),
                                                  token.getToken().toString(), getFrontendUrl() );

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

        Optional<PasswordRecoveryToken> maybeToken = passwordRecoveryTokenService.findByToken(
                tokenPasswordDto.getToken() );

        if ( !maybeToken.isPresent() ||
             maybeToken.get().getExpiresIn().isBefore( LocalDateTime.now( ZoneOffset.UTC ) ) ||
             maybeToken.get().isUsed() || !maybeToken.get().getUser().isVerified() ) {
            return Response.status( Response.Status.NOT_FOUND ).build();
        }

        User user = maybeToken.get().getUser();

        userService.changePassword( user, maybeToken.get(), tokenPasswordDto.getPassword() );

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
