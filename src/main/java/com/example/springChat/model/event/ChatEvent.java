package com.example.springChat.model.event;

import org.springframework.web.reactive.socket.WebSocketSession;

public class ChatEvent {
    protected String address;
    protected WebSocketSession session;

    public String getAddress() {
        return address;
    }

    public WebSocketSession getSession() {
        return session;
    }
}
