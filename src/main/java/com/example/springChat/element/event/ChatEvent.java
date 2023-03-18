package com.example.springChat.element.event;

import org.springframework.web.reactive.socket.WebSocketSession;

public class ChatEvent {
    protected int address;
    protected WebSocketSession session;

    protected ChatEvent(String message, WebSocketSession session){
        this.address = Integer.parseInt(message.substring(1,11));
        this.session = session;
    }

    public int getAddress() {
        return address;
    }

    public WebSocketSession getSession() {
        return session;
    }
}
