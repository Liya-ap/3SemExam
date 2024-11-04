package dat.response;

import io.javalin.http.HttpStatus;
import lombok.Getter;

@Getter
public class HttpMessage {

    private final int status;
    private final String message;

    public HttpMessage(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpMessage(HttpStatus status, String message) {
        this(status.getCode(), message);
    }
}
