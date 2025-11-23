package websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/video-status/{user_id}")
public class VideoStatusSocket {

	private static final Map<String, CopyOnWriteArraySet<Session>> rooms = new ConcurrentHashMap<>();

	private static final Map<String, String> sessionRooms = new ConcurrentHashMap<>();

	@OnOpen
	public void onOpen(Session session, @PathParam("user_id") String user_id) {
		rooms.computeIfAbsent(user_id, k -> new CopyOnWriteArraySet<>()).add(session);
		sessionRooms.put(session.getId(), user_id);
	}

	@OnClose
	public void onClose(Session session) {
		String user_id = sessionRooms.remove(session.getId());
		if (user_id != null) {
			CopyOnWriteArraySet<Session> room = rooms.get(user_id);
			if (room != null) {
				room.remove(session);
				if (room.isEmpty())
					rooms.remove(user_id);
			}
		}
		System.out.println("WS disconnected: " + session.getId());
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		System.err.println("WS error: " + throwable.getMessage());
	}

	@OnMessage
	public void onMessage(String msg, Session session) {
		String userId = sessionRooms.get(session.getId());

		if (userId != null) {
			System.out.println("Broadcasting to room " + userId + ": " + msg);
			broadcast(userId, msg);
		}
	}

	public static void broadcast(String userId, String jsonMessage) {
		CopyOnWriteArraySet<Session> room = rooms.get(userId);
		if (room != null) {
			room.forEach(s -> {
				if (s.isOpen()) {
					try {
						s.getBasicRemote().sendText(jsonMessage);

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}

	}
}
