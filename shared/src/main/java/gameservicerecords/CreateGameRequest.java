package gameservicerecords;

public record CreateGameRequest(String authToken, String gameName) {
}
