package com.example.springChat.element.event;

import org.springframework.web.reactive.socket.WebSocketSession;

public class CreateChatEvent extends ChatEvent{
    public static final char ID = 'c';

    public CreateChatEvent(String message, WebSocketSession session) {
        super(message, session);
    }
}
