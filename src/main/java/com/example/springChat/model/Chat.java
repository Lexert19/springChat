package com.example.springChat.model;

import com.example.springChat.element.Message;
import com.example.springChat.element.event.UpdateChatEvent;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.*;

@Table(name="chats")
public class Chat {
    @Id
    private int id;

    private String rawChat;
    @Transient
    private List<Message> messages;
    @Transient
    private List<UpdateChatEvent> updateChatRequests;
    @Transient
    private Map<String, WebSocketSession> attachedSessions;



    public Chat() {
        //this.messages = new LinkedList<>();
        this.messages = Collections.synchronizedList(new LinkedList<Message>());
        this.updateChatRequests = Collections.synchronizedList(new LinkedList<>());
        this.attachedSessions = Collections.synchronizedMap(new HashMap<>());
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

    public List<UpdateChatEvent> getUpdateChatRequests(){
        return updateChatRequests;
    }

    public void clearChatRequests(){
        updateChatRequests.clear();
    }

    public List<Message> getMessages() {
        return messages;
    }

    public Map<String, WebSocketSession> getAttachedSessions() {
        return attachedSessions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void updateRawChat(){
        StringBuilder text = new StringBuilder();
        for(Message message : getMessages()){
            text.append(message.getMessage());
            text.append('|');
        }
        this.rawChat = text.toString();
    }
}
