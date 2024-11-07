package exception;

/**
 * Indicates there was an unauthorized access attempt. 401 error
 */
public class UnauthorizedException extends ResponseException {

    public UnauthorizedException() {
        super(401, "Error: unauthorized");
    }

    public UnauthorizedException(String message) {
        super(401, message);
    }
}
