package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ArrayList<Session>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, Session session) {
        connections.computeIfAbsent(gameID, k -> new ArrayList<>()).add(session);
    }

    public void remove(int gameID, Session session) {
        if (connections.containsKey(gameID)) {
            connections.get(gameID).remove(session);
        }
    }

    public void broadcast(int gameID, String message, Session excludeSession) {
        if (!connections.containsKey(gameID)) {
            return;
        }
        var removeList = new ArrayList<Session>();
        for (Session session : connections.get(gameID)) {
            if (!session.isOpen()) {
                removeList.add(session);
                continue;
            }
            if (!session.equals(excludeSession)) {
                try {
                    session.getRemote().sendString(message);
                } catch (Exception e) {
                    removeList.add(session);
                }
            }
        }
        connections.get(gameID).removeAll(removeList);
    }
}

