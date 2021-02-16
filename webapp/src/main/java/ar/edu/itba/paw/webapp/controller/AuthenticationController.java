package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.MailingService;
import ar.edu.itba.paw.interfaces.PasswordRecoveryTokenService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.PasswordRecoveryToken;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.auth.LoginHelper;
import ar.edu.itba.paw.webapp.dto.authentication.AuthDto;
import ar.edu.itba.paw.webapp.dto.authentication.AuthRequestDto;
import ar.edu.itba.paw.webapp.dto.authentication.PasswordRecoveryDto;
import ar.edu.itba.paw.webapp.dto.authentication.SignUpDto;
import ar.edu.itba.paw.webapp.dto.authentication.TokenPasswordDto;
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
import java.util.Optional;
import java.util.Set;

@Component
@Path( "/auth" )
public class AuthenticationController extends BaseController
{
    @Autowired
    private UserService userService;

    @Autowired
    private LoginHelper loginHelper;

    @Autowired
    private Validator validator;

    @Autowired
    private MailingService mailingService;

    @Autowired
    private PasswordRecoveryTokenService passwordRecoveryTokenService;

    @Context
    private UriInfo uriInfo;

    @POST
    @Path( "/login" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public Response login( @RequestBody AuthRequestDto authRequestDto ) {
        String email = authRequestDto.getEmail();
        String password = authRequestDto.getPassword();

        Optional<AuthDto> maybeAuth = loginHelper.authenticate( email, password );

        if ( !maybeAuth.isPresent() ) {
            return Response.status( Response.Status.UNAUTHORIZED ).build();
        }

        userService.findByUsername( email ).ifPresent( user -> maybeAuth.get().setUser( UserDto.fromUser( user ) ) );

        return Response.ok().entity( maybeAuth.get() ).build();
    }

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
                                        signUpDto.getPassword(), signUpDto.getBirthdayLocalDate(),
                                        signUpDto.getNationality(), null );

        return Response.ok().entity( UserDto.fromUser( user ) ).build();
    }

    @POST
    @Path( "/password-recovery" )
    public Response passwordRecovery( @RequestBody PasswordRecoveryDto passwordRecoveryDto ) {
        Set<ConstraintViolation<PasswordRecoveryDto>> violations = validator.validate( passwordRecoveryDto );

        if ( !violations.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST ).entity(
                    ErrorsDto.fromConstraintsViolations( violations ) ).build();
        }

        Optional<User> maybeUser = userService.findByUsername( passwordRecoveryDto.getEmail() );

        if ( !maybeUser.isPresent() ) {
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
            return Response.status( Response.Status.BAD_REQUEST ).entity(
                    ErrorsDto.fromConstraintsViolations( violations ) ).build();
        }

        Optional<PasswordRecoveryToken> maybeToken = passwordRecoveryTokenService.findByToken(
                tokenPasswordDto.getToken() );

        if ( !maybeToken.isPresent() || maybeToken.get().getExpiresIn().isBefore( LocalDateTime.now() ) ||
             maybeToken.get().isUsed() ) {
            return Response.status( Response.Status.NOT_FOUND ).build();
        }

        passwordRecoveryTokenService.markAsUsed(maybeToken.get());

        User user = maybeToken.get().getUser();

        userService.changePassword( user, tokenPasswordDto.getPassword() );

        return Response.ok().build();
    }
}
