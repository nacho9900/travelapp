package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.TripMember;

import java.util.List;
import java.util.stream.Collectors;

public class CommentListDto
{
    private final TripMemberDto member;
    private final List<CommentDto> comments;

    public CommentListDto( TripMemberDto member, List<CommentDto> comments ) {
        this.member = member;
        this.comments = comments;
    }

    public TripMemberDto getMember() {
        return member;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public static CommentListDto fromMemberAndComments( TripMember member, List<TripComment> comments ) {
        return new CommentListDto( TripMemberDto.fromTripMember( member, false, false ), comments.stream()
                                                                                                 .map( x -> CommentDto.fromComment(
                                                                                                         x, true ) )
                                                                                                 .collect(
                                                                                                         Collectors.toList() ) );
    }
}
