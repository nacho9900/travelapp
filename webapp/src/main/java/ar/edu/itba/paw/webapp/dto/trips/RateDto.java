package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.TripRate;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.ws.rs.core.UriInfo;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class RateDto
{
    private Long id;
    private int rate;
    @JsonFormat( shape = JsonFormat.Shape.STRING,
                 pattern = "yyyy-MM-dd hh:MM:ss" )
    private Date createdOn;
    private TripMemberDto member;

    public RateDto() {
        //For Jackson
    }

    public Long getId() {
        return id;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public int getRate() {
        return rate;
    }

    public TripRate toTripRate() {
        LocalDateTime createdOn = this.createdOn.toInstant()
                                                .atZone( ZoneId.systemDefault() )
                                                .toLocalDateTime();

        return new TripRate( this.id, this.rate, createdOn, this.member != null ? this.member.toTripMember() : null );
    }

    public static RateDto fromTripRate( TripRate tripRate, boolean includeMember ) {
        RateDto rateDto = new RateDto();
        rateDto.id = tripRate.getId();
        rateDto.createdOn = java.sql.Timestamp.valueOf( tripRate.getCreatedOn() );

        if ( includeMember ) {
            TripMember member = tripRate.getMember();
            if ( member != null ) {
                rateDto.member = TripMemberDto.fromTripMember( member, false, false );
            }
        }

        return rateDto;
    }
}
