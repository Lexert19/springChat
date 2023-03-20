package com.example.springChat.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

@Table(name = "users")
public class User {
    @Id
    private int id;
    private String name;
    private String email;
    private String password;




    @Transient
    private WebSocketSession session;
    @Transient
    private HashMap<Integer, Chat> attachedChats;

    public User(WebSocketSession session) {
        attachedChats = new HashMap<>();
        this.session = session;
    }

    public User(String name) {
        this.name = name;
    }

    public void joinChat(Chat chat){
        attachedChats.put(chat.getId(), chat);
    }

    public void removeFromChats(){
        for(Map.Entry<Integer, Chat> entry : attachedChats.entrySet()){
            Chat chat = entry.getValue();
            chat.getAttachedSessions().remove(session.getId());
            attachedChats.remove(chat.getId());
        }

    }

    public WebSocketSession getSession() {
        return session;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }
}
