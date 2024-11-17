package server.websocket;

public class BaseService {
    GameConnectionManager gameConnectionManager;

    public BaseService(GameConnectionManager gameConnectionManager) {
        this.gameConnectionManager = gameConnectionManager;
    }
}
