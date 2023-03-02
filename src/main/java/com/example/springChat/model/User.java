package com.example.springChat.model;

import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

public class User {
    private WebSocketSession session;
    private HashMap<Long,Chat> attachedChats;

    public User(WebSocketSession session) {
        attachedChats = new HashMap<>();
        this.session = session;
    }

    public void joinChat(Chat chat){
        attachedChats.put(chat.getAddress(), chat);
    }

    public void removeFromChats(){
        for(Map.Entry<Long, Chat> entry : attachedChats.entrySet()){
            Chat chat = entry.getValue();
            chat.getAttachedSessions().remove(session.getId());
            attachedChats.remove(chat.getAddress());
        }

    }


    public WebSocketSession getSession() {
        return session;
    }
}
