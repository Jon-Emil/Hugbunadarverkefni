package is.hi.hbv501g.hbv1.extras.DTOs;

import java.util.List;

/**
 * This is a simple wrapper class for the final output of our API
 * @param <T> the type of object the data list is supposed to contain
 * used when we want to send paginated lists to the user
 */

public class PaginatedResponse<T> extends BaseResponse<T> {
    private int total;
    private int pageNr;
    private int perPage;

    public PaginatedResponse(int status, List<T> data, int pageNr, int perPage) {
        this.setStatus(status);
        this.setMessage("");
        this.total = data.size();
        this.pageNr = pageNr;
        this.perPage = perPage;

        this.setData(paginate(data, pageNr, perPage));
    }

    public PaginatedResponse(int status, List<T> data, int pageNr, int perPage, String message) {
        this.setStatus(status);
        this.setMessage(message);
        this.total = data.size();
        this.pageNr = pageNr;
        this.perPage = perPage;

        this.setData(paginate(data, pageNr, perPage));
    }

    private List<T> paginate(List<T> data, int pageNr, int perPage) {
        int startIndex = Math.min((pageNr - 1) * perPage, data.size());
        int endIndex = Math.min(startIndex + perPage, data.size());
        return List.copyOf(data.subList(startIndex, endIndex));
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