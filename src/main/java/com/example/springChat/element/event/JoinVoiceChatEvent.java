package com.example.springChat.element.event;

import org.springframework.web.reactive.socket.WebSocketSession;

public class JoinVoiceChatEvent extends ChatEvent {
    public static final char ID = 'p';


    public JoinVoiceChatEvent(String message, WebSocketSession session) {
        super(message, session);
    }
}
