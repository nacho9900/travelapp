package ar.edu.itba.paw.model;

import java.util.LinkedList;
import java.util.List;

public class PaginatedResult<T>
{
    private final List<T> result;
    private final long total;
    private final int itemsPerPage;
    private final int page;

    public PaginatedResult( List<T> result, long total, int itemsPerPage, int page ) {
        this.result = result;
        this.total = total;
        this.itemsPerPage = itemsPerPage;
        this.page = page;
    }

    public List<T> getResult() {
        return result;
    }

    public boolean hasNextPage() {
        return this.page != getLastPage();
    }

    public boolean hasPreviousPage() {
        return this.page > 1;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public long getLastPage() {
        return (long) Math.ceil( (double) this.total / (double) this.itemsPerPage );
    }

    public int getPage() {
        return page;
    }

    public boolean isEmpty() {
        return result.isEmpty();
    }

    public static <T> PaginatedResult<T> getEmpty() {
        final List<T> emptyList = new LinkedList<>();
        return new PaginatedResult<>( emptyList, 0,  0, 0);
    }
}
