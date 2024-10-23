package service;

import exception.ResponseException;

/**
 * Indicates there was an unauthorized access attempt. 403 error
 */
public class ForbiddenException extends ResponseException {
    public ForbiddenException(String message) {
        super(403, "Error: already taken");
    }
}
