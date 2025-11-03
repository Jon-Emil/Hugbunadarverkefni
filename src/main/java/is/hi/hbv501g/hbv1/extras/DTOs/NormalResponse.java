package is.hi.hbv501g.hbv1.extras.DTOs;

import java.util.List;

public class NormalResponse<T> {
    private int status;
    private List<T> data;
    private String message;

    public NormalResponse(int status, String message, List<T> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public NormalResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = List.of(data);
    }

    public NormalResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.data = List.of();
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
