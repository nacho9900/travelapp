package ar.edu.itba.paw.model;

import java.util.List;

public class PaginatedResult<T>
{
    private final List<T> result;
    private final long total;
    private final boolean hasNextPage;
    private final int itemsPerPage;

    public PaginatedResult( List<T> result, long total,int itemsPerPage, boolean hasNextPage ) {
        this.result = result;
        this.total = total;
        this.hasNextPage = hasNextPage;
        this.itemsPerPage = itemsPerPage;
    }

    public List<T> getResult() {
        return result;
    }

    public long getTotal() {
        return total;
    }

    public boolean hasNextPage() {
        return hasNextPage;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }
}
