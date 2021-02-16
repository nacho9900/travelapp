package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.TripComment;
import ar.edu.itba.paw.model.TripMember;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.cglib.core.Local;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@JsonInclude( JsonInclude.Include.NON_NULL )
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
    private LocalDateTime createdOn;
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

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public TripComment toTripComment() {
        LocalDateTime createdOn = this.createdOn;

        return new TripComment( this.id, this.member != null ? this.member.toTripMember() : null, this.comment,
                createdOn );
    }

    public static CommentDto fromComment( TripComment tripComment, boolean includeMember ) {
        CommentDto commentDto = new CommentDto();
        commentDto.id = tripComment.getId();
        commentDto.comment = tripComment.getComment();
        commentDto.createdOn = tripComment.getCreatedOn();

        if ( includeMember ) {
            TripMember tripMember = tripComment.getMember();
            if ( tripMember != null ) {
                commentDto.member = TripMemberDto.fromTripMember( tripMember, true, false );

            }
        }


        return commentDto;
    }
}
