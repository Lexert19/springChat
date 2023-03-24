package com.example.springChat.element.event;

import org.springframework.web.reactive.socket.WebSocketSession;

public class LeaveVoiceChatEvent extends ChatEvent{
    public static final char ID = 'o';

    public LeaveVoiceChatEvent(String message, WebSocketSession session) {
        super(message, session);
    }
}
