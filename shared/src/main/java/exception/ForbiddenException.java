package exception;

/**
 * Indicates there was an unauthorized access attempt. 403 error
 */
public class ForbiddenException extends ResponseException {

    public ForbiddenException() {
        super(403, "Error: forbidden");
    }

    public ForbiddenException(String message) {
        super(403, message);
    }
}
