package com.example.springChat.element.event;

import org.springframework.web.reactive.socket.WebSocketSession;

public class AddUserToChatEvent extends ChatEvent{
    public static final char ID = 'a';
    private int userId;

    public AddUserToChatEvent(String message, WebSocketSession session) {
        super(message, session);
        userId = Integer.parseInt(message.substring(11));
    }

    public int getUserId() {
        return userId;
    }
}
