package com.example.springChat.element;

import org.springframework.web.reactive.socket.WebSocketSession;

public class Message {
    private WebSocketSession session;
    private String sessionId;
    private int userId;
    private String message;

    public Message(String message, WebSocketSession session) {
        this.message = message;
        this.sessionId = session.getId();
    }

    public Message(String message) {
        this.message = message;
    }

    public Message(int userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getMessage() {
        return message;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
