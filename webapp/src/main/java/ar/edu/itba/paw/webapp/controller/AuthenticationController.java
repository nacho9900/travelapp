package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.webapp.auth.LoginHelper;
import ar.edu.itba.paw.webapp.dto.authentication.AuthDto;
import ar.edu.itba.paw.webapp.dto.authentication.AuthRequestDto;
import ar.edu.itba.paw.webapp.dto.authentication.SignUpDto;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.dto.users.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
    private LoginHelper loginHelper;

    @Autowired
    private Validator validator;

    @Context
    private UriInfo uriInfo;

    @POST
    @Path( "/login" )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response login( @RequestBody AuthRequestDto authRequestDto ) {
        String email = authRequestDto.getEmail();
        String password = authRequestDto.getPassword();

        Optional<AuthDto> maybeAuth = loginHelper.authenticate( email, password );

        if ( maybeAuth.isPresent() ) {
            return Response.ok( maybeAuth.get() )
                           .build();
        }
        else {
            return Response.status( Response.Status.UNAUTHORIZED )
                           .build();
        }
    }

    @POST
    @Path( "/signup" )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response signup( @RequestBody SignUpDto signUpDto ) {
        Set<ConstraintViolation<SignUpDto>> violations = validator.validate( signUpDto );

        if ( !violations.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .build();
        }

        User user = userService.create( signUpDto.getFirstname(), signUpDto.getLastname(), signUpDto.getEmail(),
                signUpDto.getPassword(), signUpDto.getBirthdayLocalDate(), signUpDto.getNationality() );

        return Response.ok( UserDto.fromUser( user, uriInfo ) )
                       .build();
    }
}
