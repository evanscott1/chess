package service;

/**
 * Indicates there was an unauthorized access attempt. 403 error
 */
public class ForbiddenException extends Exception {
    public ForbiddenException(String message) {
        super(message);
    }
}
