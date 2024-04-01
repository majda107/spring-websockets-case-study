package com.example.springwebsocketscasestudy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


public class ChatWebSocketHub extends TextWebSocketHandler implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private ChatRoomService _chatService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        initializeServices();
    }

    // dependency injection
    private void initializeServices() {
        this._chatService = applicationContext.getBean(ChatRoomService.class);
    }



    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String chatRoomId = getChatRoomId(session);
        _chatService.joinRoom(chatRoomId, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String chatRoomId = getChatRoomId(session);
        _chatService.sendMessageToChatRoom(chatRoomId, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String chatRoomId = getChatRoomId(session);
        _chatService.leaveRoom(chatRoomId, session);
    }



    // **************************************
    // Session content extraction helpers
    // **************************************
    private String getChatRoomId(WebSocketSession session) {
        return session.getUri().getQuery().split("=")[1];
    }
}