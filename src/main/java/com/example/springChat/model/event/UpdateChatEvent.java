package com.example.springChat.model.event;

import org.springframework.web.reactive.socket.WebSocketSession;

public class UpdateChatEvent extends ChatEvent{
    public static char id = 'u';

    public UpdateChatEvent(String message, WebSocketSession session){
        address = message.substring(1,11);
        this.session = session;
    }

    public WebSocketSession getSession() {
        return session;
    }
}
