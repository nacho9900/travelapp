package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.TripRate;

import javax.ws.rs.core.UriInfo;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class RateDto
{
    private Long id;
    private int rate;
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

    public TripMemberDto getMember() {
        return member;
    }

    public int getRate() {
        return rate;
    }

    public TripRate toTripRate() {
        LocalDateTime createdOn = this.createdOn.toInstant()
                                                .atZone( ZoneId.systemDefault() )
                                                .toLocalDateTime();

        return new TripRate( this.id, this.rate, createdOn, this.member.toTripMember() );
    }

    public static RateDto fromTripRate( TripRate tripRate, UriInfo uriInfo, boolean includeMember ) {
        RateDto rateDto = new RateDto();
        rateDto.id = tripRate.getId();
        rateDto.createdOn = java.sql.Timestamp.valueOf( tripRate.getCreatedOn() );

        if ( includeMember ) {
            TripMember member = tripRate.getMember();
            if ( member != null ) {
                rateDto.member = TripMemberDto.fromTripMember( member, uriInfo, false, false );
            }
        }

        return rateDto;
    }
}
