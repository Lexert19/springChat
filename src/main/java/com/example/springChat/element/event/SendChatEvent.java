package com.example.springChat.element.event;

import org.springframework.web.reactive.socket.WebSocketSession;

public class SendChatEvent extends ChatEvent {
    public static char id = 's';
    private String message;

    public SendChatEvent(String message, WebSocketSession session){
        super(message, session);
        this.message = message.substring(11);
    }

    public String getMessage() {
        return message;
    }
}
