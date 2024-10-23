package service;

import exception.ResponseException;

/**
 * Indicates there was an unauthorized access attempt. 401 error
 */
public class UnauthorizedException extends ResponseException {
    public UnauthorizedException(String message) {
        super(401, "Error: unauthorized");
    }
}
