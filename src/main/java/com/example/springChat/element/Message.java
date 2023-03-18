package com.example.springChat.element;

import org.springframework.web.reactive.socket.WebSocketSession;

public class Message {
    private WebSocketSession session;
    private String sessionId;
    private String message;

    public Message(String message, WebSocketSession session) {
        this.message = message;
        this.sessionId = session.getId();
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
}
