package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.TripMember;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class CommentDto
{
    private Long id;
    @NotNull
    @NotBlank
    @Length( max = 160 )
    private String comment;
    @NotNull
    @JsonFormat( shape = JsonFormat.Shape.STRING,
                 pattern = "yyyy-MM-dd hh:MM:ss" )
    private Date createdOn;
    @NotNull
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

    public Date getCreatedOn() {
        return createdOn;
    }

    public TripComment toTripComment() {
        LocalDateTime createdOn = this.createdOn.toInstant()
                                                .atZone( ZoneId.systemDefault() )
                                                .toLocalDateTime();

        return new TripComment( this.id, this.member != null ? this.member.toTripMember() : null, this.comment,
                createdOn );
    }

    public static CommentDto fromComment( TripComment tripComment, boolean includeMember ) {
        CommentDto commentDto = new CommentDto();
        commentDto.id = tripComment.getId();
        commentDto.comment = tripComment.getComment();
        commentDto.createdOn = java.sql.Timestamp.valueOf( tripComment.getCreatedOn() );

        if ( includeMember ) {
            TripMember tripMember = tripComment.getMember();
            if ( tripMember != null ) {
                commentDto.member = TripMemberDto.fromTripMember( tripMember, true, false );

            }
        }


        return commentDto;
    }
}
