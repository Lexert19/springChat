package com.example.springChat.element.event;

import org.springframework.web.reactive.socket.WebSocketSession;

public class SaveChatEvent extends ChatEvent{
    public static final char ID = 'd';

    public SaveChatEvent(String message, WebSocketSession session) {
        super(message, session);
    }
}
