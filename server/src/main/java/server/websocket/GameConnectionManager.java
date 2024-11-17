package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class GameConnectionManager {

    public final ConcurrentHashMap<Integer, ConnectionManager> games = new ConcurrentHashMap<>();

    public GameConnectionManager() {

    }

    public void add(Integer gameID, ConnectionManager connectionManager) {

        games.put(gameID, connectionManager);
    }

    public ConnectionManager get(int gameID) {
        return games.get(gameID);
    }

}
