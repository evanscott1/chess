package client;

import exception.BadRequestException;
import exception.ResponseException;

public class ExceptionHandler {


    public static void handleResponseException(int errorCode) throws ResponseException {
        switch (errorCode) {
            case(400) -> throw new BadRequestException("Please check your command is typed correctly.");
            case(401) -> throw new BadRequestException("Please check your command is typed correctly.");
            case(403) -> throw new BadRequestException("Chess is unable to process your request.");
            case(500) -> throw new BadRequestException("There is an issue with the chess server.");
            default -> throw new ResponseException(404, "Something went wrong.");
        }
    }

}
