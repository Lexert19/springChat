package com.example.springChat.element.event;

import org.springframework.web.reactive.socket.WebSocketSession;

public class JoinChatEvent extends ChatEvent{
    public static char ID = 'j';

    public JoinChatEvent(String message, WebSocketSession session) {
        super(message, session);
    }


}
