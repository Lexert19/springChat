package com.example.springChat.element.event;

import org.springframework.web.reactive.socket.WebSocketSession;

public class LoginChatEvent extends ChatEvent{
    public static final char ID = 'l';

    public LoginChatEvent(String message, WebSocketSession session) {
        super(message, session);
    }
}
