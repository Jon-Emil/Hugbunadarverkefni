package is.hi.hbv501g.hbv1.extras.DTOs;

import java.util.List;

/**
 * This is a simple wrapper class for the final output of our API
 * @param <T> the type of object the data list is supposed to contain
 * used when we want to send paginated lists to the user
 */

public class PaginatedResponse<T> {
    private int status;
    private List<T> data;
    private int total;
    private int pageNr;
    private int perPage;

    public PaginatedResponse(int status, List<T> data, int pageNr, int perPage) {
        this.status = status;
        this.total = data.size();
        this.pageNr = pageNr;
        this.perPage = perPage;

        int startIndex = Math.min((pageNr - 1) * perPage, data.size());
        int endIndex = Math.min(startIndex + perPage, data.size());
        this.data = data.subList(startIndex, endIndex);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageNr() {
        return pageNr;
    }

    public void setPageNr(int pageNr) {
        this.pageNr = pageNr;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }
}