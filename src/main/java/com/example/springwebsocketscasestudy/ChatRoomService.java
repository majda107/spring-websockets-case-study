package com.example.springwebsocketscasestudy;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatRoomService {
    private final Map<String, Set<WebSocketSession>> chatRooms = new ConcurrentHashMap<>();

    public void joinRoom(String chatRoomId, WebSocketSession session) {
        chatRooms.computeIfAbsent(chatRoomId, k -> new HashSet<>()).add(session);
    }

    public void leaveRoom(String chatRoomId, WebSocketSession session) {
        Set<WebSocketSession> sessions = chatRooms.get(chatRoomId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                chatRooms.remove(chatRoomId);
            }
        }
    }

    public void sendMessageToChatRoom(String chatRoomId, TextMessage message) throws IOException {
        Set<WebSocketSession> sessions = chatRooms.get(chatRoomId);
        if (sessions != null) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            }
        }
    }
}
