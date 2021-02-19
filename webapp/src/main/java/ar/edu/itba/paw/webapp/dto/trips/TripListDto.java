package ar.edu.itba.paw.webapp.dto.trips;

import ar.edu.itba.paw.model.PaginatedResult;
import ar.edu.itba.paw.model.Trip;

import java.util.List;
import java.util.stream.Collectors;

public class TripListDto
{
    private final List<TripDto> trips;
    private final long total;
    private final boolean hasNextPages;
    private final int itemsPerPage;

    public TripListDto( List<TripDto> trips, long total, int itemsPerPage, boolean hasMorePages ) {
        this.hasNextPages = hasMorePages;
        this.total = total;
        this.trips = trips;
        this.itemsPerPage = itemsPerPage;
    }

    public List<TripDto> getTrips() {
        return trips;
    }

    public long getTotal() {
        return total;
    }

    public boolean isHasNextPages() {
        return hasNextPages;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public static TripListDto fromPaginatedResult( PaginatedResult<Trip> result ) {
        return new TripListDto( result.getResult().stream().map( TripDto::fromTrip ).collect( Collectors.toList() ),
                                result.getTotal(), result.getItemsPerPage(), result.hasNextPage() );
    }
}
