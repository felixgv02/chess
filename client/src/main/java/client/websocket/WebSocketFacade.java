package client.websocket;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ErrorMessage;
import jakarta.websocket.*;
import java.net.URI;

public class WebSocketFacade extends Endpoint{

    private Session session;
    private final ServerMessageObserver observer;
    public WebSocketFacade(String url, ServerMessageObserver observer) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.observer = observer;
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            this.session.setMaxIdleTimeout(0); // Disable client-side timeout

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    try {
                        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                        switch (serverMessage.getServerMessageType()) {
                            case LOAD_GAME -> observer.notify(new Gson().fromJson(message, LoadGameMessage.class));
                            case NOTIFICATION -> observer.notify(new Gson().fromJson(message, NotificationMessage.class));
                            case ERROR -> observer.notify(new Gson().fromJson(message, ErrorMessage.class));
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println(ui.EscapeSequences.SET_TEXT_COLOR_YELLOW + "SYSTEM DISCONNECT: " + closeReason.getCloseCode() + " -> " + closeReason.getReasonPhrase() + ui.EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    public void sendCommand(UserGameCommand command) throws Exception {
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
    }

}
