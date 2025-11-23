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

@ServerEndpoint("/videos/{video_id}")
public class WatchVideoSocket{

	private static final Map<String, CopyOnWriteArraySet<Session>> rooms = new ConcurrentHashMap<>();

	private static final Map<String, String> sessionRooms = new ConcurrentHashMap<>();

	@OnOpen
	public void onOpen(Session session, @PathParam("video_id") String video_id) {

		rooms.computeIfAbsent(video_id, k -> new CopyOnWriteArraySet<>()).add(session);
		sessionRooms.put(session.getId(), video_id);

	}

	@OnClose
	public void onClose(Session session) {
		String videoId = sessionRooms.remove(session.getId());

		if (videoId != null) {
			CopyOnWriteArraySet<Session> room = rooms.get(videoId);
			if (room != null) {
				room.remove(session);

			
				if (room.isEmpty()) {
					rooms.remove(videoId);
					
				} 
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
		String videoId = sessionRooms.get(session.getId());

		if (videoId != null) {
			System.out.println("Broadcasting to room " + videoId + ": " + msg);
			broadcast(videoId, msg);
		}
	}


	public static void broadcast(String videoId, String jsonMessage) {
		CopyOnWriteArraySet<Session> room = rooms.get(videoId);
		
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
