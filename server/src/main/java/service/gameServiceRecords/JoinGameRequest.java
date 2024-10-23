package service.gameServiceRecords;

public record JoinGameRequest(String authToken, String playerColor, int gameID) {
}
