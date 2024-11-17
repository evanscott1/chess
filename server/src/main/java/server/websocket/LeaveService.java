package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.ConnectCommand;

public class LeaveService extends BaseService {
    public LeaveService(GameConnectionManager gameConnectionManager) {
        super(gameConnectionManager);
    }
}
