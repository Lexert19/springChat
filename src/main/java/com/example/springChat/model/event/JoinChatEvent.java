package com.example.springChat.model.event;

import org.springframework.web.reactive.socket.WebSocketSession;

public class JoinChatEvent extends ChatEvent{
    public static char ID = 'j';

    public JoinChatEvent(String message, WebSocketSession session) {
        this.address = message.substring(1,11);
        this.session = session;
    }
}
