package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.ActivityService;
import ar.edu.itba.paw.interfaces.TripCommentsService;
import ar.edu.itba.paw.interfaces.TripJoinRequestService;
import ar.edu.itba.paw.interfaces.TripMemberService;
import ar.edu.itba.paw.interfaces.TripPicturesService;
import ar.edu.itba.paw.interfaces.TripService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.model.Activity;
import ar.edu.itba.paw.model.PaginatedResult;
import ar.edu.itba.paw.model.Trip;
import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.TripJoinRequest;
import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.TripPicture;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exception.ActivityNotPartOfTheTripException;
import ar.edu.itba.paw.model.exception.CannotDeleteOwnerException;
import ar.edu.itba.paw.model.exception.EntityNotFoundException;
import ar.edu.itba.paw.model.exception.ImageFormatException;
import ar.edu.itba.paw.model.exception.ImageMaxSizeException;
import ar.edu.itba.paw.model.exception.InvalidDateRangeException;
import ar.edu.itba.paw.model.exception.InvalidUserException;
import ar.edu.itba.paw.model.exception.UserAlreadyAMemberException;
import ar.edu.itba.paw.model.exception.UserAlreadyHaveAPendingRequestException;
import ar.edu.itba.paw.model.exception.UserNotMemberException;
import ar.edu.itba.paw.model.exception.UserNotOwnerOrAdminException;
import ar.edu.itba.paw.webapp.dto.errors.ErrorDto;
import ar.edu.itba.paw.webapp.dto.errors.ErrorsDto;
import ar.edu.itba.paw.webapp.dto.general.FileDto;
import ar.edu.itba.paw.webapp.dto.trips.ActivityDto;
import ar.edu.itba.paw.webapp.dto.trips.ActivityListDto;
import ar.edu.itba.paw.webapp.dto.trips.CommentDto;
import ar.edu.itba.paw.webapp.dto.trips.CommentListDto;
import ar.edu.itba.paw.webapp.dto.trips.JoinTripDto;
import ar.edu.itba.paw.webapp.dto.trips.PlaceDto;
import ar.edu.itba.paw.webapp.dto.trips.TripDto;
import ar.edu.itba.paw.webapp.dto.trips.TripJoinRequestDto;
import ar.edu.itba.paw.webapp.dto.trips.TripListDto;
import ar.edu.itba.paw.webapp.dto.trips.TripMemberDto;
import ar.edu.itba.paw.webapp.dto.trips.TripMemberListDto;
import ar.edu.itba.paw.webapp.utils.MyQueryParam;
import ar.edu.itba.paw.webapp.utils.PaginatedResultResponseHelper;
import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private TripPicturesService tripPicturesService;

    @Autowired
    private Validator validator;

    @Autowired
    private GeoApiContext geoApiContext;

    @Autowired
    private TripMemberService tripMemberService;

    @Autowired
    private TripJoinRequestService tripJoinRequestService;

    @Autowired
    private TripCommentsService tripCommentsService;

    @Context
    private UriInfo uriInfo;

    //region trip

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

        tripDto.toTrip();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            Trip trip = tripService.create( username, tripDto.getName(), tripDto.getDescription(),
                                            tripDto.getStartDate(), tripDto.getEndDate() );

            return Response.created( uriInfo.getAbsolutePathBuilder().path( Long.toString( trip.getId() ) ).build() )
                           .entity( TripDto.fromTrip( trip, uriInfo ) )
                           .build();
        }
        catch ( InvalidUserException e ) {
            return Response.serverError().build();
        }
        catch ( InvalidDateRangeException e ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( new ErrorDto( "Invalid Date Range" ) )
                           .build();
        }
    }

    @GET
    @Path( "/{id}" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response get( @PathParam( "id" ) long id ) {
        Optional<Trip> maybeTrip = tripService.findById( id );

        if ( !maybeTrip.isPresent() ) {
            return Response.status( Response.Status.NOT_FOUND ).build();
        }

        TripDto tripDto = TripDto.fromTrip( maybeTrip.get(), uriInfo );

        return Response.ok().entity( tripDto ).build();
    }

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public Response getAllByUserId(
            @QueryParam( "page" ) @DefaultValue( "1" ) int page,
            @QueryParam( "perPage" ) @DefaultValue( "12" ) int perPage ) {
        if ( page <= 0 ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( new ErrorDto( "invalid page", "page" ) )
                           .build();
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            PaginatedResult<Trip> result = tripService.getAllUserTrips( username, page, perPage );

            return PaginatedResultResponseHelper.makeResponseBuilder( result, uriInfo )
                                                .entity( TripListDto.fromPaginatedResult( result, uriInfo ) )
                                                .build();
        }
        catch ( EntityNotFoundException e ) {
            return Response.status( Response.Status.NOT_FOUND ).build();
        }
    }

    @PUT
    @Path( "/{id}" )
    public Response update( @PathParam( "id" ) long id, @RequestBody TripDto tripDto ) {
        Set<ConstraintViolation<TripDto>> violations = validator.validate( tripDto );

        if ( !violations.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( ErrorsDto.fromConstraintsViolations( violations ) )
                           .build();
        }

        if ( tripDto.getId() == null || id != tripDto.getId() ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( new ErrorDto( "Invalid Trip id", "id" ) )
                           .build();
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            Trip trip = tripService.update( id, tripDto.getName(), tripDto.getDescription(), tripDto.getStartDate(),
                                            tripDto.getEndDate(), username );
            return Response.ok().entity( TripDto.fromTrip( trip, uriInfo ) ).build();

        }
        catch ( EntityNotFoundException e ) {
            return tripNotFound();
        }
        catch ( UserNotOwnerOrAdminException e ) {
            return Response.status( Response.Status.UNAUTHORIZED ).build();
        }
        catch ( InvalidDateRangeException e ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( new ErrorDto( "Invalid Date Range" ) )
                           .build();
        }
    }

    @GET
    @Path( "/search" )
    @Produces( MediaType.APPLICATION_JSON )
    public Response search(
            @QueryParam( "latitude" ) Double latitude,
            @QueryParam( "longitude" ) Double longitude,
            @QueryParam( "from" ) String fromString,
            @QueryParam( "to" ) String toString,
            @QueryParam( "perPage" ) @DefaultValue( "12" ) int perPage,
            @QueryParam( "page" ) @DefaultValue( "1" ) int page ) {
        ErrorsDto errors = new ErrorsDto();
        List<MyQueryParam> myQueryParams = new LinkedList<>();

        LocalDate from = null;
        LocalDate to = null;

        if ( fromString != null ) {
            try {
                from = LocalDate.parse( fromString );
                myQueryParams.add( new MyQueryParam( "from", fromString ) );
            }
            catch ( DateTimeParseException e ) {
                errors.addError( "invalid from date format", "from" );
            }
        }

        if ( toString != null ) {
            try {
                to = LocalDate.parse( toString );
                myQueryParams.add( new MyQueryParam( "to", toString ) );
            }
            catch ( DateTimeParseException e ) {
                errors.addError( "invalid to date format", "to" );
            }
        }

        if ( page <= 0 ) {
            errors.addError( "invalid page number", "page" );
        }

        if ( latitude != null ) {
            if ( latitude < -90 || latitude > 90 ) {
                errors.addError( "invalid latitude", "latitude" );
            }
            else {
                myQueryParams.add( new MyQueryParam( "latitude", latitude ) );
            }
        }

        if ( longitude != null ) {
            if ( longitude < -180 || longitude > 180 ) {
                errors.addError( "invalid longitude", "longitude" );
            }
            else {
                myQueryParams.add( new MyQueryParam( "longitude", longitude ) );
            }
        }

        if ( !errors.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST ).entity( errors ).build();
        }

        PaginatedResult<Trip> results = tripService.search( latitude, longitude, from, to, page, perPage );

        return PaginatedResultResponseHelper.makeResponseBuilder( results, uriInfo, myQueryParams )
                                            .entity( TripListDto.fromPaginatedResult( results, uriInfo ) )
                                            .build();
    }

    //endregion

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
            TripPicture tripPicture = tripPicturesService.createOrUpdate( id, fileDto.getFilename(),
                                                                          fileDto.getFileBase64(), username );

            return Response.created(
                    uriInfo.getAbsolutePathBuilder().path( Long.toString( tripPicture.getId() ) ).build() ).build();
        }
        catch ( ImageMaxSizeException | ImageFormatException e ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( new ErrorDto( "Invalid file format or size" ) )
                           .build();
        }
        catch ( UserNotOwnerOrAdminException e ) {
            return Response.status( Response.Status.UNAUTHORIZED ).build();
        }
        catch ( EntityNotFoundException e ) {
            return tripNotFound();
        }
    }

    @GET
    @Path( "/{id}/picture" )
    @Produces( "image/png" )
    public Response getPicture(
            @PathParam( "id" ) long id,
            @QueryParam( "width" ) Integer width, @QueryParam( "height" ) Integer height, @Context Request request ) {
        Optional<TripPicture> maybeTripPicture = tripPicturesService.findByTripId( id );

        if ( !maybeTripPicture.isPresent() ) {
            return Response.status( Response.Status.NOT_FOUND ).build();
        }

        TripPicture tripPicture = maybeTripPicture.get();

        EntityTag etag;

        if ( width != null && height != null ) {
            etag = new EntityTag( Integer.toString( tripPicture.hashCode() * width.hashCode() * height.hashCode() ) );
            tripPicture.setPicture( tripPicturesService.resize( tripPicture.getPicture(), width, height ) );
        }
        else if ( width != null ) {
            etag = new EntityTag( Integer.toString( tripPicture.hashCode() * width.hashCode() * 7 ) );
            tripPicture.setPicture( tripPicturesService.resizeWidth( tripPicture.getPicture(), width ) );
        }
        else if ( height != null ) {
            etag = new EntityTag( Integer.toString( tripPicture.hashCode() * height.hashCode() * 11 ) );
            tripPicture.setPicture( tripPicturesService.resizeHeight( tripPicture.getPicture(), height ) );
        }
        else {
            etag = new EntityTag( Integer.toString( tripPicture.hashCode() ) );
        }

        Response.ResponseBuilder builder = request.evaluatePreconditions( etag );

        CacheControl cc = new CacheControl();
        cc.setMaxAge( 3600 );

        if ( builder == null ) {
            return Response.ok( new ByteArrayInputStream( tripPicture.getPicture() ) )
                           .tag( etag )
                           .cacheControl( cc )
                           .build();
        }
        else {
            return builder.cacheControl( cc ).build();
        }
    }

    //endregion

    //region activity

    @POST
    @Path( "/{id}/activity" )
    public Response createActivity( @PathParam( "id" ) long id, @RequestBody ActivityDto activityDto ) {
        Set<ConstraintViolation<ActivityDto>> activityValidation = validator.validate( activityDto );
        Set<ConstraintViolation<PlaceDto>> placeValidation = validator.validate( activityDto.getPlace() );

        if ( !activityValidation.isEmpty() || !placeValidation.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( ErrorsDto.fromConstraintsViolations( activityValidation )
                                             .addConstraintsViolations( placeValidation ) )
                           .build();
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Activity activity;
        try {
            activity = activityService.create( id, activityDto.getName(), activityDto.getStartDate(),
                                               activityDto.getEndDate(), activityDto.getPlace().toPlace(), username );

            return Response.created(
                    uriInfo.getAbsolutePathBuilder().path( Long.toString( activity.getId() ) ).build() )
                           .entity( ActivityDto.fromActivity( activity, uriInfo, id ) )
                           .build();
        }
        catch ( EntityNotFoundException | UserNotOwnerOrAdminException e ) {
            return tripNotFound();
        }
        catch ( InvalidDateRangeException e ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( new ErrorDto( "Invalid Date Range", "startDate endDate" ) )
                           .build();
        }
    }

    @GET
    @Path( "/{id}/activity" )
    public Response getActivities( @PathParam( "id" ) long id ) {
        Optional<Trip> maybeTrip = tripService.findById( id );

        if ( !maybeTrip.isPresent() ) {
            return tripNotFound();
        }

        ActivityListDto activities = ActivityListDto.fromActivityList( activityService.findByTrip( id ), uriInfo, id );

        return Response.ok().entity( activities ).build();
    }

    @PUT
    @Path( "/{id}/activity/{activityId}" )
    public Response updateActivity(
            @PathParam( "id" ) long id,
            @PathParam( "activityId" ) long activityId, @RequestBody ActivityDto activityDto ) {
        Set<ConstraintViolation<ActivityDto>> activityValidation = validator.validate( activityDto );
        Set<ConstraintViolation<PlaceDto>> placeValidation = validator.validate( activityDto.getPlace() );

        if ( !activityValidation.isEmpty() || !placeValidation.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( ErrorsDto.fromConstraintsViolations( activityValidation )
                                             .addConstraintsViolations( placeValidation ) )
                           .build();
        }

        if ( activityDto.getId() == null || activityId != activityDto.getId() ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( new ErrorDto( "the activity does not contains id", "id" ) )
                           .build();
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            Activity activity = activityService.update( activityId, id, activityDto.getName(),
                                                        activityDto.getStartDate(), activityDto.getEndDate(),
                                                        activityDto.getPlace().toPlace(), username );

            return Response.ok().entity( ActivityDto.fromActivity( activity, uriInfo, id ) ).build();
        }
        catch ( EntityNotFoundException | UserNotOwnerOrAdminException e ) {
            return tripNotFound();
        }
        catch ( InvalidDateRangeException e ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( new ErrorDto( "Invalid Date Range", "startDate endDate" ) )
                           .build();
        }
        catch ( ActivityNotPartOfTheTripException e ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( new ErrorDto( "the activity is not part of the trip" ) )
                           .build();
        }
    }

    @DELETE
    @Path( "/{id}/activity/{activityId}" )
    public Response deleteActivity( @PathParam( "id" ) long id, @PathParam( "activityId" ) long activityId ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            activityService.delete( activityId, id, username );
        }
        catch ( EntityNotFoundException | ActivityNotPartOfTheTripException | UserNotOwnerOrAdminException e ) {
            return Response.status( Response.Status.NO_CONTENT ).build();
        }

        return Response.ok().build();
    }

    //endregion

    //region member

    @GET
    @Path( "/{id}/member" )
    public Response getAllMembers( @PathParam( "id" ) long id, @QueryParam( "email" ) String email ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            TripMemberListDto tripMemberListDto;

            if ( email != null && !email.isEmpty() ) {
                Optional<TripMember> maybeTripMember = tripMemberService.findByTripIdAndUsername( id, email, username );

                tripMemberListDto = maybeTripMember.map( tripMember -> TripMemberListDto.fromMembersList(
                        Stream.of( tripMember ).collect( Collectors.toList() ), uriInfo, id ) )
                                                   .orElseGet(
                                                           () -> TripMemberListDto.fromMembersList( new LinkedList<>(),
                                                                                                    uriInfo, id ) );
            }
            else {
                tripMemberListDto = TripMemberListDto.fromMembersList( tripMemberService.getAllByTripId( id, username ),
                                                                       uriInfo, id );
            }

            return Response.ok().entity( tripMemberListDto ).build();
        }
        catch ( UserNotMemberException e ) {
            return Response.status( Response.Status.FORBIDDEN ).build();
        }
    }

    @DELETE
    @Path( "/{id}/member/{memberId}" )
    public Response deleteMember(
            @PathParam( "id" ) long id,
            @PathParam( "memberId" ) long memberId, @Context HttpServletRequest httpRequest ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            tripService.deleteMember( id, memberId, username, httpRequest.getLocale() );
        }
        catch ( EntityNotFoundException | UserNotOwnerOrAdminException | UserNotMemberException e ) {
            return Response.status( Response.Status.NO_CONTENT ).build();
        }
        catch ( CannotDeleteOwnerException e ) {
            return Response.status( Response.Status.FORBIDDEN )
                           .entity( new ErrorDto( "cannot delete trip owner" ) )
                           .build();
        }

        return Response.ok().build();
    }

    //endregion

    //region joinrequest

    @POST
    @Path( "/{id}/join" )
    public Response joinTrip(
            @PathParam( "id" ) long id,
            @RequestBody JoinTripDto joinTripDto, @Context HttpServletRequest httpRequest ) {
        Set<ConstraintViolation<JoinTripDto>> violations = validator.validate( joinTripDto );

        if ( !violations.isEmpty() ) {
            return Response.status( Response.Status.BAD_REQUEST )
                           .entity( ErrorsDto.fromConstraintsViolations( violations ) )
                           .build();
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            TripJoinRequest request = tripJoinRequestService.create( username, id, joinTripDto.getMessage(),
                                                                     httpRequest.getLocale() );
            return Response.ok().entity( TripJoinRequestDto.fromTripJoinRequest( request, uriInfo, id ) ).build();
        }
        catch ( EntityNotFoundException e ) {
            return tripNotFound();
        }
        catch ( UserAlreadyAMemberException | UserAlreadyHaveAPendingRequestException e ) {
            return Response.status( Response.Status.CONFLICT )
                           .entity( new ErrorDto( "User already a member or have a pending request" ) )
                           .build();
        }
    }

    @GET
    @Path( "/{id}/join" )
    public Response getAllJoinRequest( @PathParam( "id" ) long id, @QueryParam( "email" ) String email ) {
        Optional<Trip> maybeTrip = tripService.findById( id );
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if ( !maybeTrip.isPresent() ) {
            return tripNotFound();
        }

        if ( !tripMemberService.isUserOwnerOrAdmin( id, username ) && ( email == null || email.isEmpty() ) ) {
            return Response.status( Response.Status.UNAUTHORIZED ).build();
        }

        List<TripJoinRequest> joinRequests = tripJoinRequestService.getAllPendingByTripId( id );

        if ( email != null && !email.isEmpty() ) {
            return Response.ok()
                           .entity( joinRequests.stream()
                                                .filter( x -> x.getUser().getEmail().equals( email ) )
                                                .map( x -> TripJoinRequestDto.fromTripJoinRequest( x, uriInfo, id ) ) )
                           .build();
        }

        return Response.ok()
                       .entity( joinRequests.stream()
                                            .map( x -> TripJoinRequestDto.fromTripJoinRequest( x, uriInfo, id ) ) )
                       .build();
    }

    @POST
    @Path( "/{id}/join/{requestId}/accept" )
    public Response acceptRequest(
            @PathParam( "id" ) long id,
            @PathParam( "requestId" ) long requestId, @Context HttpServletRequest httpRequest ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            TripMember tripMember = tripJoinRequestService.accept( requestId, username, httpRequest.getLocale() );
            return Response.ok().entity( TripMemberDto.fromTripMember( tripMember, uriInfo, id ) ).build();
        }
        catch ( EntityNotFoundException e ) {
            return Response.status( Response.Status.NOT_FOUND )
                           .entity( new ErrorDto( "join request not found" ) )
                           .build();
        }
        catch ( UserNotOwnerOrAdminException | UserNotMemberException e ) {
            return Response.status( Response.Status.UNAUTHORIZED ).build();
        }
    }

    @POST
    @Path( "/{id}/join/{requestId}/reject" )
    public Response rejectRequest( @PathParam( "id" ) long id, @PathParam( "requestId" ) long requestId ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            tripJoinRequestService.reject( requestId, username );
        }
        catch ( EntityNotFoundException e ) {
            return Response.status( Response.Status.NOT_FOUND )
                           .entity( new ErrorDto( "join request not found" ) )
                           .build();
        }
        catch ( UserNotOwnerOrAdminException e ) {
            return Response.status( Response.Status.UNAUTHORIZED ).build();
        }

        return Response.ok().build();
    }

    //endregion

    //region comments

    @POST
    @Path( "/{id}/comment" )
    public Response createComment( @PathParam( "id" ) long id, @RequestBody CommentDto commentDto ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            TripComment comment = tripCommentsService.create( id, username, commentDto.getComment() );

            return Response.created( uriInfo.getAbsolutePathBuilder().path( Long.toString( comment.getId() ) ).build() )
                           .entity( CommentDto.fromCommentWithMember( comment, uriInfo, id ) )
                           .build();
        }
        catch ( UserNotMemberException e ) {
            return Response.status( Response.Status.NOT_FOUND )
                           .entity( new ErrorDto( "Member or Trip not found" ) )
                           .build();
        }
    }

    @GET
    @Path( "/{id}/comment" )
    public Response getAllComments( @PathParam( "id" ) long id ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            List<TripComment> comments = tripCommentsService.getAllByTripId( id, username );

            return Response.ok().entity( CommentListDto.fromComments( comments, uriInfo, id ) ).build();
        }
        catch ( UserNotMemberException e ) {
            return Response.status( Response.Status.UNAUTHORIZED ).build();
        }
    }

    //endregion

    private static Response tripNotFound() {
        return Response.status( Response.Status.NOT_FOUND ).entity( new ErrorDto( "Trip not Found" ) ).build();
    }
}
