package service.GameServiceRecords;

public record CreateGameRequest(String authToken, String gameName) {
}
