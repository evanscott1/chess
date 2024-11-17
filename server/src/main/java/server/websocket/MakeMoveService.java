package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.ConnectCommand;

public class MakeMoveService extends BaseService {


    public MakeMoveService(GameConnectionManager gameConnectionManager) {
        super(gameConnectionManager);
    }
}
