package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.TripMemberRole;
import ar.edu.itba.paw.model.TripRate;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.dto.errors.ErrorsDto;
import ar.edu.itba.paw.webapp.dto.trips.TripDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Path( "/trip" )
public class TripController
{
    @Autowired
    private UserService userService;

    @Autowired
    private TripService tripService;

    @Autowired
    private Validator validator;

    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public Response create( @RequestBody TripDto tripDto ) {
        Set<ConstraintViolation<TripDto>> violations = validator.validate( tripDto );

        if ( !violations.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( ErrorsDto.fromConstraintsViolations( violations ) )
                           .build();
        }

        Trip trip = tripDto.toTrip();

        Optional<User> owner = userService.findByUsername( SecurityContextHolder.getContext()
                                                                                .getAuthentication()
                                                                                .getName() );

        if ( !owner.isPresent() ) {
            return Response.serverError()
                           .build();
        }

        List<TripMember> initialMember = new LinkedList<>();
        TripRate rate = new TripRate( 3, LocalDateTime.now() );
        TripMember member = new TripMember( trip, TripMemberRole.OWNER, true, owner.get(), rate, new LinkedList<>() );
        member.setRate( rate );

        initialMember.add( member );

        trip.setMembers( initialMember );

        tripService.create( trip );

        return Response.ok()
                       .build();
    }
}
