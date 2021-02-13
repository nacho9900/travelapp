package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.ActivityService;
import ar.edu.itba.paw.interfaces.PlaceService;
import ar.edu.itba.paw.interfaces.TripMemberService;
import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.TripMemberRole;
import ar.edu.itba.paw.model.TripRate;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.dto.errors.ErrorDto;
import ar.edu.itba.paw.webapp.dto.errors.ErrorsDto;
import ar.edu.itba.paw.webapp.dto.trips.ActivityDto;
import ar.edu.itba.paw.webapp.dto.trips.PlaceDto;
import ar.edu.itba.paw.webapp.dto.trips.RateDto;
import ar.edu.itba.paw.webapp.dto.trips.TripDto;
import ar.edu.itba.paw.webapp.dto.trips.TripMemberDto;
import ar.edu.itba.paw.webapp.dto.trips.TripMemberUpdateDto;
import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Path( "/trip" )
public class TripController
{
    @Autowired
    private UserService userService;

    @Autowired
    private TripService tripService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private Validator validator;

    @Autowired
    private GeoApiContext geoApiContext;

    @Autowired
    private TripMemberService tripMemberService;

    //region trip

    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    public Response create( @RequestBody TripDto tripDto ) {
        Set<ConstraintViolation<TripDto>> violations = validator.validate( tripDto );

        if ( !violations.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST ).entity(
                    ErrorsDto.fromConstraintsViolations( violations ) ).build();
        }

        Trip trip = tripDto.toTrip();

        Optional<User> owner = userService.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName() );

        if ( !owner.isPresent() ) {
            return Response.serverError().build();
        }

        List<TripMember> initialMember = new LinkedList<>();
        TripRate rate = new TripRate( 3, LocalDateTime.now() );
        TripMember member = new TripMember( trip, TripMemberRole.OWNER, true, owner.get(), rate, new LinkedList<>() );
        member.setRate( rate );

        initialMember.add( member );

        trip.setMembers( initialMember );

        tripService.create( trip );

        return Response.ok().entity( TripDto.fromTrip( trip ) ).build();
    }

    @GET
    @Path( "/{id}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response get( @PathParam( "id" ) long id ) {
        Optional<Trip> maybeTrip = tripService.findById( id );

        if ( !maybeTrip.isPresent() ) {
            return Response.status( Response.Status.NOT_FOUND ).build();
        }

        return Response.ok().entity( TripDto.fromTrip( maybeTrip.get() ) ).build();
    }

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Response getAll() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> maybeUser = userService.findByUsername( username );

        if ( !maybeUser.isPresent() ) {
            return Response.status( Response.Status.NOT_FOUND ).build();
        }

        List<Trip> trips = tripService.getAllUserTrips( maybeUser.get() );

        return Response.ok().entity( trips.stream().map( TripDto::fromTrip ).collect( Collectors.toList() ) ).build();
    }

    @PUT
    @Path( "/{id}" )
    public Response update( @PathParam( "id" ) long id, @RequestBody TripDto tripDto ) {
        Set<ConstraintViolation<TripDto>> violations = validator.validate( tripDto );

        if ( !violations.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST ).entity(
                    ErrorsDto.fromConstraintsViolations( violations ) ).build();
        }

        if ( tripDto.getId() == null || id != tripDto.getId() ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( new ErrorDto( "Invalid Trip id", "id" ) )
                           .build();
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<TripMember> maybeLoggedMember = tripMemberService.findByTripIdAndUsername( username, id );

        if ( !maybeLoggedMember.isPresent() || maybeLoggedMember.get().getRole() == TripMemberRole.MEMBER ) {
            return Response.status( Response.Status.UNAUTHORIZED ).build();
        }

        Optional<Trip> maybeTrip = tripService.findById( id );
        Trip tripUpdates = tripDto.toTrip();

        if ( !maybeTrip.isPresent() ) {
            return TripNotFound();
        }

        Trip trip = maybeTrip.get();

        trip = tripService.update( trip, tripUpdates.getName(), tripUpdates.getDescription(),
                                   tripUpdates.getStartDate(), tripUpdates.getEndDate() );

        return Response.ok().entity( TripDto.fromTrip( trip ) ).build();
    }

    //endregion

    //region activity

    @POST
    @Path( "/{id}/activity" )
    public Response createActivity( @PathParam( "id" ) long id, @RequestBody ActivityDto activityDto ) {
        Set<ConstraintViolation<ActivityDto>> activityValidation = validator.validate( activityDto );
        Set<ConstraintViolation<PlaceDto>> placeValidation = validator.validate( activityDto.getPlace() );

        if ( !activityValidation.isEmpty() || !placeValidation.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST ).entity(
                    ErrorsDto.fromConstraintsViolations( activityValidation )
                             .addConstraintsViolations( placeValidation ) ).build();
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Trip> maybeTrip = tripService.findById( id );

        if ( !tripService.isUserOwnerOrAdmin( id, username ) || !maybeTrip.isPresent() ) {
            return TripNotFound();
        }

        Activity activity = activityDto.toActivity();

        Place place = getOrCreatePlaceIfNotExists( activity.getPlace() );

        activity = activityService.create( activity.getName(), place, maybeTrip.get(), activity.getStartDate(),
                                           activity.getEndDate() );

        return Response.ok().entity( ActivityDto.fromActivity( activity ) ).build();
    }

    @GET
    @Path( "/{id}/activity" )
    public Response getActivities( @PathParam( "id" ) long id ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if ( !tripService.isUserOwnerOrAdmin( id, username ) ) {
            return Response.status( Response.Status.NOT_FOUND ).build();
        }

        List<ActivityDto> activities = activityService.findByTrip( id )
                                                      .stream()
                                                      .map( ActivityDto::fromActivity )
                                                      .collect( Collectors.toList() );

        return Response.ok().entity( activities ).build();
    }

    @PUT
    @Path( "/{id}/activity" )
    public Response updateActivity( @PathParam( "id" ) long id, @RequestBody ActivityDto activityDto ) {
        Set<ConstraintViolation<ActivityDto>> activityValidation = validator.validate( activityDto );
        Set<ConstraintViolation<PlaceDto>> placeValidation = validator.validate( activityDto.getPlace() );

        if ( !activityValidation.isEmpty() || !placeValidation.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST ).entity(
                    ErrorsDto.fromConstraintsViolations( activityValidation )
                             .addConstraintsViolations( placeValidation ) ).build();
        }

        if ( activityDto.getId() == null ) {
            return Response.status( Response.Status.BAD_REQUEST ).entity(
                    new ErrorDto( "the activity does not contains id", "id" ) ).build();
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Trip> maybeTrip = tripService.findById( id );

        if ( !tripService.isUserOwnerOrAdmin( id, username ) || !maybeTrip.isPresent() ||
             !activityService.isActivityPartOfTheTrip( maybeTrip.get().getId(), activityDto.getId() ) ) {
            return TripNotFound();
        }

        Activity activity = activityDto.toActivity();
        Place place = getOrCreatePlaceIfNotExists( activityDto.getPlace().toPlace() );
        activity.setPlace( place );
        activity.setTrip( maybeTrip.get() );
        activity = activityService.update( activity );

        return Response.ok().entity( ActivityDto.fromActivity( activity ) ).build();
    }

    @DELETE
    @Path( "/{id}/activity/{activityId}" )
    public Response deleteActivity( @PathParam( "id" ) long id, @PathParam( "activityId" ) long activityId ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Trip> maybeTrip = tripService.findById( id );
        Optional<Activity> maybeActivity = activityService.findById( activityId );

        if ( !tripService.isUserOwnerOrAdmin( id, username ) || !maybeTrip.isPresent() || !maybeActivity.isPresent() ||
             !activityService.isActivityPartOfTheTrip( maybeTrip.get().getId(), maybeActivity.get().getId() ) ) {
            return Response.status( Response.Status.NO_CONTENT ).build();
        }

        activityService.delete( activityId );

        return Response.ok().build();
    }


    private Place getOrCreatePlaceIfNotExists( Place place ) {
        Optional<Place> maybePlace = placeService.findByGoogleId( place.getGoogleId() );

        if ( maybePlace.isPresent() ) {
            place = maybePlace.get();
        }
        else {
            place = placeService.create( place.getGoogleId(), place.getName(), place.getLatitude(),
                                         place.getLongitude(), place.getAddress() );
        }
        return place;
    }

    //endregion

    //region member

    @GET
    @Path( "/{id}/member" )
    public Response getAllMembers( @PathParam( "id" ) long id ) {
        Optional<Trip> maybeTrip = tripService.findById( id );
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if ( !maybeTrip.isPresent() || !tripService.isUserMember( id, username ) ) {
            return TripNotFound();
        }

        return Response.ok()
                       .entity( tripMemberService.getAllByTripId( id )
                                                 .stream()
                                                 .map( x -> TripMemberDto.fromTripMember( x, true, false ) ) )
                       .build();
    }

    @DELETE
    @Path( "/{id}/member/{memberId}" )
    public Response deleteMember( @PathParam( "id" ) long id, @PathParam( "memberId" ) long memberId ) {
        Optional<Trip> maybeTrip = tripService.findById( id );
        Optional<TripMember> maybeMember = tripMemberService.findById( memberId );
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<TripMember> maybeLoggedMember = tripMemberService.findByTripIdAndUsername( username, id );

        if ( !maybeTrip.isPresent() || !maybeMember.isPresent() || !maybeLoggedMember.isPresent() ||
             !tripMemberService.memberBelongsToTheTrip( memberId, id ) ) {
            return Response.status( Response.Status.NO_CONTENT ).build();
        }

        if ( maybeLoggedMember.get().getRole() == TripMemberRole.MEMBER ||
             maybeMember.get().getRole() == TripMemberRole.OWNER ) {
            return Response.status( Response.Status.UNAUTHORIZED ).build();
        }

        tripMemberService.delete( memberId );

        return Response.ok().build();
    }

    @PUT
    @Path( "/{id}/member/{memberId}" )
    public Response updateMember(
            @PathParam( "id" ) long id,
            @PathParam( "memberId" ) long memberId, @RequestBody TripMemberUpdateDto tripMemberUpdateDto ) {
        Optional<Trip> maybeTrip = tripService.findById( id );
        Optional<TripMember> maybeMember = tripMemberService.findById( memberId );
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<TripMember> maybeLoggedMember = tripMemberService.findByTripIdAndUsername( username, id );

        if ( !maybeTrip.isPresent() || !maybeMember.isPresent() || !maybeLoggedMember.isPresent() ||
             !tripMemberService.memberBelongsToTheTrip( memberId, id ) ) {
            return TripNotFound();
        }

        Set<ConstraintViolation<TripMemberUpdateDto>> violations = validator.validate( tripMemberUpdateDto );

        if ( !violations.isEmpty() ) {
            Response.status( Response.Status.BAD_REQUEST )
                    .entity( ErrorsDto.fromConstraintsViolations( violations ) )
                    .build();
        }

        TripMemberRole newRole = TripMemberRole.valueOf( tripMemberUpdateDto.getRole() );

        if ( maybeLoggedMember.get().getRole() == TripMemberRole.MEMBER ||
             maybeMember.get().getRole() == TripMemberRole.OWNER || newRole == TripMemberRole.OWNER ) {
            return Response.status( Response.Status.UNAUTHORIZED ).build();
        }

        TripMember member = maybeMember.get();

        if ( newRole != member.getRole() ) {
            member = tripMemberService.update( member );
        }

        return Response.ok().entity( TripMemberDto.fromTripMember( member, true, false ) ).build();
    }

    @PUT
    @Path( "/{id}/rate/{rate}" )
    public Response changeRate( @PathParam( "id" ) long id, @PathParam( "rate" ) int rate ) {
        Optional<Trip> maybeTrip = tripService.findById( id );
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<TripMember> maybeLoggedMember = tripMemberService.findByTripIdAndUsername( username, id );

        if ( !maybeTrip.isPresent() || !maybeLoggedMember.isPresent() ) {
            return TripNotFound();
        }

        if ( rate < 1 || rate > 5 ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( new ErrorDto( "Invalid Rate Value" ) )
                           .build();
        }

        TripRate rateEntity = maybeLoggedMember.get().getRate();
        rateEntity.setRate( rate );
        rateEntity = tripMemberService.update( maybeLoggedMember.get() ).getRate();
        return Response.ok().entity( RateDto.fromTripRate( rateEntity, false ) ).build();
    }

    //endregion

    private static Response TripNotFound() {
        return Response.status( Response.Status.NOT_FOUND ).entity( new ErrorDto( "Trip not Found" ) ).build();
    }
}
