package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.TripComment;

import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

public class CommentListDto
{
    private final List<CommentDto> comments;

    public CommentListDto( List<CommentDto> comments ) {
        this.comments = comments;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public static CommentListDto fromComments( List<TripComment> comments, UriInfo uriInfo, long tripId ) {
        return new CommentListDto( comments.stream()
                                           .map( x -> CommentDto.fromCommentWithMember( x, uriInfo, tripId ) )
                                           .collect( Collectors.toList() ) );
    }
}
