package li.zhuoran.survey.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorInfo<T> {

    private HttpStatus code;

    private String message;

    private String url;

    private T data;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpStatus getCode() {
        return code;
    }

    public void setCode(HttpStatus code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResponseEntity<ErrorInfo<T>> toResponseEntity() {
        return new ResponseEntity<>(this, this.code);
    }
}