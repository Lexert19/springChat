package com.example.springChat.model.event;

import org.springframework.web.reactive.socket.WebSocketSession;

public class SendChatEvent extends ChatEvent {
    public static char id = 's';
    private String message;

    public SendChatEvent(String message, WebSocketSession session){
        this.address = message.substring(1,11);
        this.session = session;
        this.message = message.substring(11);
    }

    public String getMessage() {
        return message;
    }
}
