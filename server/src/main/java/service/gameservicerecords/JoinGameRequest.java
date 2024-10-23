package service.gameservicerecords;

public record JoinGameRequest(String authToken, String playerColor, int gameID) {
}
