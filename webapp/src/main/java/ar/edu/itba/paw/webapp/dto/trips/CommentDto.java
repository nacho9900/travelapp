package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.TripMember;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDateTime;

@JsonInclude( JsonInclude.Include.NON_NULL )
public class CommentDto
{
    private Long id;
    @NotNull
    @NotBlank
    @Length( max = 160 )
    private String comment;
    private LocalDateTime createdOn;
    private TripMemberDto member;

    public CommentDto() {
        //For Jackson
    }

    public Long getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public TripMemberDto getMember() {
        return member;
    }

    public static CommentDto fromCommentWithMember( TripComment tripComment, UriInfo uriInfo, long tripId ) {
        CommentDto commentDto = new CommentDto();
        commentDto.id = tripComment.getId();
        commentDto.comment = tripComment.getComment();
        commentDto.createdOn = tripComment.getCreatedOn();

        TripMember tripMember = tripComment.getMember();
        if ( tripMember != null ) {
            commentDto.member = TripMemberDto.fromTripMember( tripMember, uriInfo, tripId );
        }

        return commentDto;
    }
}
