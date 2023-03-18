package com.example.springChat.element.event;

import org.springframework.web.reactive.socket.WebSocketSession;

public class UpdateChatEvent extends ChatEvent{
    public static char id = 'u';


    public UpdateChatEvent(String message, WebSocketSession session){
        super(message, session);
    }

    public WebSocketSession getSession() {
        return session;
    }
}
