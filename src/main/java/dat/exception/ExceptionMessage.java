package dat.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ExceptionMessage {
    private final int status;
    private final String message;
    private final String timestamp;

    public ExceptionMessage(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = getCurrentTimestamp();
    }

    private String getCurrentTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}
