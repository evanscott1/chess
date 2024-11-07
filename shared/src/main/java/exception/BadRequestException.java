package exception;

/**
 * Indicates there was an unauthorized access attempt. 400 error
 */
public class BadRequestException extends ResponseException {

    public BadRequestException() {
        super(400, "Error: bad request");
    }

    public BadRequestException(String message) {
        super(400, message);
    }
}
