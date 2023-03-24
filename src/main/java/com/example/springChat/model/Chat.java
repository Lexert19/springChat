package com.example.springChat.model;

import com.example.springChat.element.Message;
import com.example.springChat.element.event.SendAudioChatEvent;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Table(name = "chats")
public class Chat {
    @Id
    @Expose
    private int id;
    @Expose
    private String jsonMessages;
    @Expose
    private String jsonUsers;

    @Transient
    private Map<Integer, ChatAccess> users;
    @Transient
    private List<Message> messages;
    //@Transient
    //private List<UpdateChatEvent> updateChatRequests;
    @Transient
    private Map<String, WebSocketSession> attachedSessions;
    @Transient
    private Map<String, WebSocketSession> voiceSessions;
    @Transient
    private Gson gson;


    public Chat() {
        users = new ConcurrentHashMap<>();
        gson = new Gson();
        messages = Collections.synchronizedList(new LinkedList<>());
        //updateChatRequests = Collections.synchronizedList(new LinkedList<>());
        voiceSessions = new ConcurrentHashMap<>();
        attachedSessions = new ConcurrentHashMap<>();
    }

    public Chat(User owner) {
        users = new ConcurrentHashMap<>();
        gson = new Gson();
        messages = Collections.synchronizedList(new LinkedList<>());
        //updateChatRequests = Collections.synchronizedList(new LinkedList<>());
        voiceSessions = new ConcurrentHashMap<>();
        attachedSessions = new ConcurrentHashMap<>();

        ChatAccess access = new ChatAccess(owner.getId(), ChatAccess.Role.OWNER);
        users.put(owner.getId(), access);
    }

    public boolean addUser(User sender, User user, ChatAccess.Role role) {
        if (users.get(sender.getId()).role == ChatAccess.Role.OWNER) {
            ChatAccess access = new ChatAccess(user.getId(), role);
            users.put(user.getId(), access);
            return true;
        }
        return false;
    }

    public boolean joinChat(User user) {
        if (!users.containsKey(user.getId()))
            return false;
        attachedSessions.put(user.getSession().getId(), user.getSession());
        return true;
    }

    public boolean addMessage(String message, int userId) {
        if (!users.containsKey(userId))
            return false;

        Message msg = new Message(userId, message);
        messages.add(msg);
        sendToAll(msg);
        return true;
    }

    public void sendToAll(Message message) {
        for (Map.Entry<String, WebSocketSession> entry : attachedSessions.entrySet()) {
            WebSocketSession session = entry.getValue();
            if (session.isOpen()) {
                session.send(Mono.just(session.textMessage(message.getMessage()))).subscribe();
            } else {
                attachedSessions.remove(session.getId());
            }
        }
    }

    public void joinToVoiceChat(User user){
        if(!users.containsKey(user.getId()))
            return;
        voiceSessions.put(user.getSession().getId(), user.getSession());

    }

    public void leaveVoiceChat(User user){
        voiceSessions.remove(user.getSession().getId());
    }

    public void voiceChatSendAudio(SendAudioChatEvent event, User user){
        if(!users.containsKey(user.getId()))
            return;

        WebSocketMessage m = new WebSocketMessage(WebSocketMessage.Type.BINARY, event.getAudio());
        m.retain();
        for(Map.Entry<String, WebSocketSession> entry : voiceSessions.entrySet()){
            if(!user.getSession().getId().equals(entry.getKey())){

                entry.getValue().send(Mono.just(m)).subscribe();
            }
        }

    }

    public Mono<Void> updateChat(User user){

        for(Message message : messages){
            var msg = Mono.just(user.getSession().textMessage(message.getMessage()));

            user.getSession().send(msg).subscribe();
        }
        return Mono.empty();
    }

    public void convertMessagesToJsonMessages() {
        jsonMessages = gson.toJson(messages);
    }

    public void convertJsonMessagesToMessages() {
        messages = gson.fromJson(jsonMessages, new TypeToken<LinkedList<Message>>() {}.getType());
    }

    public void convertUsersToJsonUsers(){
        jsonUsers = gson.toJson(users);
    }

    public void convertJsonUsersToUsers(){
        users = gson.fromJson(jsonUsers,  new TypeToken<Map<Integer, ChatAccess>>() {}.getType());
    }

    /*public boolean addChatRequest(UpdateChatEvent event, int userId) {
        if (!users.containsKey(userId))
            return false;
        updateChatRequests.add(event);
        return true;
    }*/

   /* public List<UpdateChatEvent> getUpdateChatRequests() {
        return updateChatRequests;
    }

    public void clearChatRequests() {
        updateChatRequests.clear();
    }*/

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

}
