package client;

import exception.BadRequestException;
import exception.ForbiddenException;
import exception.ResponseException;
import exception.UnauthorizedException;

public class ExceptionHandler {


    public static void handleResponseException(int errorCode) throws ResponseException {
        switch (errorCode) {
            case(400) -> throw new BadRequestException("Please check your command is typed correctly.");
            case(401) -> throw new UnauthorizedException("Access denied. Please try again.");
            case(403) -> throw new ForbiddenException("Chess is unable to process your request.");
            case(500) -> throw new ResponseException(500, "There is an issue with the chess server.");
            default -> throw new ResponseException(404, "Something went wrong.");
        }
    }

}
