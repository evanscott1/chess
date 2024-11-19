package server.websocket;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class GameConnectionManager {

    public final ConcurrentHashMap<Integer, ConnectionManager> games = new ConcurrentHashMap<>();
    public final ArrayList<Integer> finishedGames = new ArrayList<>();

    public GameConnectionManager() {

    }

    public void addConnectionManager(Integer gameID, ConnectionManager connectionManager) {
        games.put(gameID, connectionManager);
    }

    public ConnectionManager getConnectionManager(int gameID) {
        return games.get(gameID);
    }

    public void removeConnectionManager(int gameID) {
        games.remove(gameID);
        finishedGames.add(gameID);
    }

    public boolean isFinished(int gameID) {
        return finishedGames.contains(gameID);
    }

    public void clear() {
        games.clear();
        finishedGames.clear();
    }

}
