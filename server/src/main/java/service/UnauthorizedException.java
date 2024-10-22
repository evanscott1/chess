package service;

/**
 * Indicates there was an unauthorized access attempt. 401 error
 */
public class UnauthorizedException extends Exception {
    public UnauthorizedException(String message) {
        super(message);
    }
}
