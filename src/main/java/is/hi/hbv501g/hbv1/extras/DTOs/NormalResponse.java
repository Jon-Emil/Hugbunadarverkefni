package is.hi.hbv501g.hbv1.extras.DTOs;

import java.util.Collections;
import java.util.List;

public class NormalResponse<T> extends BaseResponse<T>{
    public NormalResponse(int status, String message, List<T> data) {
        this.setStatus(status);
        this.setMessage(message);
        this.setData(data);
    }

    public NormalResponse(int status, String message, T data) {
        this.setStatus(status);
        this.setMessage(message);
        this.setData(List.of(data));
    }

    public NormalResponse(int status, String message) {
        this.setStatus(status);
        this.setMessage(message);
        this.setData(Collections.emptyList());
    }
}
