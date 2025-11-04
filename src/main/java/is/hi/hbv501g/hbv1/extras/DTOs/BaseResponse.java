package is.hi.hbv501g.hbv1.extras.DTOs;

import java.util.List;

/**
 * A super class for our response DTOs containing all things that they have in common
 *
 * @param <T> type of the data listed in the data field
 */
public abstract class BaseResponse<T> {
    private int status;
    private String message;
    private List<T> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
