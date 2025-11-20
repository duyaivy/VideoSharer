package websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/video-status")
public class VideoStatusSocket {

	private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

	@OnOpen
	public void onOpen(Session session) {
		sessions.put(session.getId(), session);
		System.out.println("WS connected: " + session.getId());
	}

	@OnClose
	public void onClose(Session session) {
		sessions.remove(session.getId());
		System.out.println("WS disconnected: " + session.getId());
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		System.err.println("WS error: " + throwable.getMessage());
	}

	@OnMessage
	public void onMessage(String msg, Session session) {
		// không cần xử lý message từ client
	}

	// server dùng hàm này để bắn trạng thái
	public static void broadcast(String jsonMessage) {
		sessions.values().forEach(s -> {
			try {
				s.getBasicRemote().sendText(jsonMessage);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
