package service;

/**
 * Indicates there was an unauthorized access attempt. 400 error
 */
public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}
