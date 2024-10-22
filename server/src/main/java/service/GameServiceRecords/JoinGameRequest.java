package service.GameServiceRecords;

public record JoinGameRequest(String authToken, String playerColor, int gameID) {
}
