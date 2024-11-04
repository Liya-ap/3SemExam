package dat.security.exception;

public class TokenCreationException extends Exception {

    public TokenCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenCreationException(String message) {
        super(message);
    }
}