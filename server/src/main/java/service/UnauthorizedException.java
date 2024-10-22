package service;

/**
 * Indicates there was an unauthorized access attempt
 */
public class UnauthorizedException extends Exception {
    public UnauthorizedException(String message) {
        super(message);
    }
}
