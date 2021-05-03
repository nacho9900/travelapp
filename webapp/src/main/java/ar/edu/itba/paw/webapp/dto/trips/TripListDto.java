package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.PaginatedResult;
import ar.edu.itba.paw.model.Trip;

import java.util.List;
import java.util.stream.Collectors;

public class TripListDto
{
    private final List<TripDto> trips;

    private TripListDto( List<TripDto> trips ) {
        this.trips = trips;
    }

    public List<TripDto> getTrips() {
        return trips;
    }

    public static TripListDto fromPaginatedResult( PaginatedResult<Trip> result ) {
        return new TripListDto( result.getResult().stream().map( TripDto::fromTrip ).collect( Collectors.toList() ) );
    }
}
