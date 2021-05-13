package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.TripMember;
import ar.edu.itba.paw.model.TripRate;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.ws.rs.core.UriInfo;
import java.time.LocalDateTime;

@JsonInclude( JsonInclude.Include.NON_NULL )
public class RateDto
{
    private Long id;
    private int rate;
    private LocalDateTime createdOn;
    private TripMemberDto member;

    public RateDto() {
        //For Jackson
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public int getRate() {
        return rate;
    }

    public static RateDto fromTripRateWithMember( TripRate tripRate, UriInfo uriInfo, long tripId ) {
        RateDto rateDto = new RateDto();
        rateDto.id = tripRate.getId();
        rateDto.createdOn = tripRate.getCreatedOn();

        TripMember member = tripRate.getMember();
        if ( member != null ) {
            rateDto.member = TripMemberDto.fromTripMember( member, uriInfo, tripId );
        }

        return rateDto;
    }
}
