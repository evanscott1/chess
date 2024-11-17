package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.ConnectCommand;

public class ResignService extends BaseService {
    public ResignService(GameConnectionManager gameConnectionManager) {
        super(gameConnectionManager);
    }
}
