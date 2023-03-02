package com.example.springChat.model;

import com.example.springChat.model.event.UpdateChatEvent;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Chat {
    private long address;
    private LinkedList<Message> messages;
    private LinkedList<UpdateChatEvent> updateChatRequests;
    private HashMap<String, WebSocketSession> attachedSessions;



    public Chat() {
        this.messages = new LinkedList<>();
        this.updateChatRequests = new LinkedList<>();
        this.attachedSessions = new HashMap<>();
    }

    public void joinChat(WebSocketSession session){
        attachedSessions.put(session.getId(), session);
    }

    public void addMessage(String message, WebSocketSession session){
        Message msg = new Message(message, session);
        messages.add(msg);
        sendToAll(msg);

    }

    public void sendToAll(Message message){
        for(Map.Entry<String, WebSocketSession> entry : attachedSessions.entrySet()){
            WebSocketSession session = entry.getValue();
            if(session.isOpen()){
                session.send(Mono.just(session.textMessage(message.getMessage()))).subscribe();
            }else {
                attachedSessions.remove(session.getId());
            }
        }
    }

    public void addChatRequest(UpdateChatEvent event){
        updateChatRequests.add(event);
    }

    public LinkedList<UpdateChatEvent> getUpdateChatRequests(){
        return updateChatRequests;
    }

    public void clearChatRequests(){
        updateChatRequests.clear();
    }

    public LinkedList<Message> getMessages() {
        return messages;
    }

    public HashMap<String, WebSocketSession> getAttachedSessions() {
        return attachedSessions;
    }

    public long getAddress() {
        return address;
    }

    public void setAddress(long address) {
        this.address = address;
    }
}
