package websocket.messages;

public class LoadGameMessage extends ServerMessage {


    public LoadGameMessage(String message) {
        super(ServerMessageType.LOAD_GAME);
        this.message = message;
    }
}
