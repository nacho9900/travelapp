package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.ActivityService;
import ar.edu.itba.paw.interfaces.PlaceService;
import ar.edu.itba.paw.interfaces.TripJoinRequestService;
import ar.edu.itba.paw.interfaces.TripMemberService;
import ar.edu.itba.paw.interfaces.TripPicturesService;
import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.Place;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripJoinRequest;
import ar.edu.itba.paw.model.TripJoinRequestStatus;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.TripMemberRole;
import ar.edu.itba.paw.model.TripPicture;
import ar.edu.itba.paw.model.TripRate;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.dto.errors.ErrorDto;
import ar.edu.itba.paw.webapp.dto.errors.ErrorsDto;
import ar.edu.itba.paw.webapp.dto.trips.ActivityDto;
import ar.edu.itba.paw.webapp.dto.trips.ActivityListDto;
import ar.edu.itba.paw.webapp.dto.trips.FileDto;
import ar.edu.itba.paw.webapp.dto.trips.JoinTripDto;
import ar.edu.itba.paw.webapp.dto.trips.PlaceDto;
import ar.edu.itba.paw.webapp.dto.trips.RateDto;
import ar.edu.itba.paw.webapp.dto.trips.TripDto;
import ar.edu.itba.paw.webapp.dto.trips.TripJoinRequestDto;
import ar.edu.itba.paw.webapp.dto.trips.TripMemberDto;
import ar.edu.itba.paw.webapp.dto.trips.TripMemberListDto;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Path( "/trip" )
public class TripController extends BaseController
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
    private TripPicturesService tripPicturesService;

    @Autowired
    private Validator validator;

    @Autowired
    private GeoApiContext geoApiContext;

    @Autowired
    private TripMemberService tripMemberService;

    @Autowired
    private TripJoinRequestService tripJoinRequestService;

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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if ( !maybeTrip.isPresent() ) {
            return Response.status( Response.Status.NOT_FOUND ).build();
        }

        TripDto tripDto = TripDto.fromTrip( maybeTrip.get() );

        tripMemberService.findByTripIdAndUsername( username, id ).ifPresent(
                tripMember -> tripDto.setRole( tripMember.getRole().name() ) );

        if ( tripDto.getRole() == null ) {
            tripJoinRequestService.getLastByTripIdAndUsername( id, username ).ifPresent(
                    tripJoinRequest -> tripDto.setUserJoinRequest(
                            TripJoinRequestDto.fromTripJoinRequest( tripJoinRequest, false ) ) );
        }

        return Response.ok().entity( tripDto ).build();
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

    //region picture

    @PUT
    @Path( "/{id}/picture" )
    public Response createOrUpdatePicture( @PathParam( "id" ) long id, @RequestBody FileDto fileDto ) {
        Set<ConstraintViolation<FileDto>> violations = validator.validate( fileDto );

        if ( !violations.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST ).entity(
                    ErrorsDto.fromConstraintsViolations( violations ) ).build();
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<TripMember> maybeLoggedMember = tripMemberService.findByTripIdAndUsername( username, id );

        if ( !maybeLoggedMember.isPresent() || maybeLoggedMember.get().getRole() == TripMemberRole.MEMBER ) {
            return Response.status( Response.Status.UNAUTHORIZED ).build();
        }

        Optional<Trip> maybeTrip = tripService.findById( id );

        if ( !maybeTrip.isPresent() ) {
            return TripNotFound();
        }

        Optional<TripPicture> maybeTripPicture = tripPicturesService.findByTripId( id );

        TripPicture tripPicture;

        if ( !maybeTripPicture.isPresent() ) {
            tripPicture = tripPicturesService.create( maybeTrip.get(), fileDto.getFilename(), fileDto.getFileBase64() );
        }
        else {
            tripPicture = tripPicturesService.update( maybeTripPicture.get(), maybeTrip.get(), fileDto.getFilename(),
                                                      fileDto.getFileBase64() );
        }

        if ( tripPicture == null ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( new ErrorDto( "Invalid Image format" ) )
                           .build();
        }

        return Response.created( uriInfo.getAbsolutePathBuilder().path( String.format( "/%d/picture", id ) ).build() )
                       .build();
    }

    @GET
    @Path( "/{id}/picture" )
    @Produces( "image/png" )
    public Response getPicture(
            @PathParam( "id" ) long id, @QueryParam( "width" ) Integer width, @QueryParam( "height" ) Integer height ) {
        Optional<TripPicture> maybeTripPicture = tripPicturesService.findByTripId( id );

        if ( !maybeTripPicture.isPresent() ) {
            return Response.status( Response.Status.NOT_FOUND ).build();
        }

        TripPicture tripPicture = maybeTripPicture.get();

        if ( width != null && height != null ) {
            tripPicture.setPicture( tripPicturesService.resize( tripPicture.getPicture(), width, height ) );
        }
        else if ( width != null ) {
            tripPicture.setPicture( tripPicturesService.resizeWidth( tripPicture.getPicture(), width ) );
        }
        else if ( height != null ) {
            tripPicture.setPicture( tripPicturesService.resizeHeight( tripPicture.getPicture(), height ) );
        }

        return Response.ok( new ByteArrayInputStream( tripPicture.getPicture() ) ).build();
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

        ActivityListDto activities = ActivityListDto.fromActivityList( activityService.findByTrip( id ) );

        tripMemberService.findByTripIdAndUsername( username, id ).ifPresent(
                tripMember -> activities.setRole( tripMember.getRole().name() ) );

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

        TripMemberListDto tripMemberListDto = TripMemberListDto.fromMembersList(
                tripMemberService.getAllByTripId( id ) );

        tripMemberService.findByTripIdAndUsername( username, id ).ifPresent(
                tripMember -> tripMemberListDto.setRole( tripMember.getRole().name() ) );

        return Response.ok().entity( tripMemberListDto ).build();
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

    @POST
    @Path( "/{id}/exit" )
    public Response exitTrip( @PathParam( "id" ) long id ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<TripMember> maybeMember = tripMemberService.findByTripIdAndUsername( username, id );

        if ( !maybeMember.isPresent() ) {
            return TripNotFound();
        }

        TripMember member = maybeMember.get();

        if ( member.getRole().equals( TripMemberRole.OWNER ) ) {
            return Response.status( Response.Status.CONFLICT ).entity(
                    new ErrorDto( "the owner of the trip cannot exit" ) ).build();
        }

        tripMemberService.delete( member.getId() );

        return Response.ok().build();
    }

    //endregion

    //region joinrequest

    @POST
    @Path( "/{id}/join" )
    public Response joinTrip( @PathParam( "id" ) long id, @RequestBody JoinTripDto joinTripDto ) {
        Set<ConstraintViolation<JoinTripDto>> violations = validator.validate( joinTripDto );

        if ( !violations.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST ).entity(
                    ErrorsDto.fromConstraintsViolations( violations ) ).build();
        }

        Optional<Trip> maybeTrip = tripService.findById( id );
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if ( !maybeTrip.isPresent() ) {
            return TripNotFound();
        }

        Optional<User> maybeUser = userService.findByUsername( username );

        if ( tripService.isUserMember( id, username ) || !maybeUser.isPresent() ) {
            return Response.status( Response.Status.CONFLICT ).entity( new ErrorDto( "already member" ) ).build();
        }

        Optional<TripJoinRequest> maybeRequest = tripJoinRequestService.getLastByTripIdAndUsername( id, username );

        if ( maybeRequest.isPresent() && maybeRequest.get().getStatus().equals( TripJoinRequestStatus.PENDING ) ) {
            return Response.status( Response.Status.CONFLICT ).entity(
                    new ErrorDto( "already have a pending join request" ) ).build();
        }

        TripJoinRequest request = tripJoinRequestService.create( maybeUser.get(), maybeTrip.get(),
                                                                 joinTripDto.getMessage() );

        return Response.ok().entity( TripJoinRequestDto.fromTripJoinRequest( request, false ) ).build();
    }

    @GET
    @Path( "/{id}/join" )
    public Response getAllJoinRequest( @PathParam( "id" ) long id ) {
        Optional<Trip> maybeTrip = tripService.findById( id );
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if ( !maybeTrip.isPresent() ) {
            return TripNotFound();
        }

        if ( !tripService.isUserOwnerOrAdmin( id, username ) ) {
            return Response.status( Response.Status.UNAUTHORIZED ).build();
        }

        List<TripJoinRequest> joinRequests = tripJoinRequestService.getAllPendingByTripId( id );

        return Response.ok().entity(
                joinRequests.stream().map( x -> TripJoinRequestDto.fromTripJoinRequest( x, true ) ) ).build();
    }

    @POST
    @Path( "/{id}/join/{requestId}/accept" )
    public Response acceptRequest( @PathParam( "id" ) long id, @PathParam( "requestId" ) long requestId ) {
        Optional<Trip> maybeTrip = tripService.findById( id );
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if ( !maybeTrip.isPresent() ) {
            return TripNotFound();
        }

        if ( !tripService.isUserOwnerOrAdmin( id, username ) ) {
            return Response.status( Response.Status.UNAUTHORIZED ).build();
        }

        Optional<TripJoinRequest> maybeJoinRequest = tripJoinRequestService.findById( requestId );

        if ( !maybeJoinRequest.isPresent() ) {
            return Response.status( Response.Status.NOT_FOUND )
                           .entity( new ErrorDto( "Join Request not found" ) )
                           .build();
        }

        TripMember tripMember = tripJoinRequestService.accept( maybeJoinRequest.get() );

        return Response.ok().entity( TripMemberDto.fromTripMember( tripMember, false, false ) ).build();
    }

    @POST
    @Path( "/{id}/join/{requestId}/reject" )
    public Response rejectRequest( @PathParam( "id" ) long id, @PathParam( "requestId" ) long requestId ) {
        Optional<Trip> maybeTrip = tripService.findById( id );
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if ( !maybeTrip.isPresent() ) {
            return TripNotFound();
        }

        if ( !tripService.isUserOwnerOrAdmin( id, username ) ) {
            return Response.status( Response.Status.UNAUTHORIZED ).build();
        }

        Optional<TripJoinRequest> maybeJoinRequest = tripJoinRequestService.findById( requestId );

        if ( !maybeJoinRequest.isPresent() ) {
            return Response.status( Response.Status.NOT_FOUND )
                           .entity( new ErrorDto( "Join Request not found" ) )
                           .build();
        }

        tripJoinRequestService.reject( maybeJoinRequest.get() );

        return Response.ok().build();
    }
    //endregion

    private static Response TripNotFound() {
        return Response.status( Response.Status.NOT_FOUND ).entity( new ErrorDto( "Trip not Found" ) ).build();
    }
}
