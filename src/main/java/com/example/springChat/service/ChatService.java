package com.example.springChat.service;

import com.example.springChat.model.Chat;
import com.example.springChat.model.Message;
import com.example.springChat.model.User;
import com.example.springChat.model.event.JoinChatEvent;
import com.example.springChat.model.event.SendChatEvent;
import com.example.springChat.model.event.UpdateChatEvent;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.*;

public class ChatService {
    private HashMap<String, Chat> chatMap;
    private LinkedList<String> messages;
    private HashSet<WebSocketSession> sessions;
    private HashMap<String, User> users;

    public ChatService() {
        chatMap = new HashMap<>();
        this.messages = new LinkedList<>();
        sessions = new HashSet<>();
        users = new HashMap<>();
    }

    public void sendMessages(){
        for(WebSocketSession session : sessions){
            if(!session.isOpen()){
                sessions.remove(session);
                continue;
            }
            for(String message : messages){

                session.send(Mono.just(session.textMessage(message))).subscribe();
            }
        }
    }

    public void updateChatRequest(UpdateChatEvent event){
        Chat chat = chatMap.get(event.getAddress());
        chat.addChatRequest(event);
    }

    public void updateChats(){
        for(Map.Entry<String, Chat> chat : chatMap.entrySet()){
            for(UpdateChatEvent event : chat.getValue().getUpdateChatRequests()){
                WebSocketSession session = event.getSession();
                for(Message message : chat.getValue().getMessages()){
                    session.send(Mono.just(session.textMessage(message.getMessage()))).subscribe();
                }
            }
            chat.getValue().clearChatRequests();
        }
    }

    public boolean addSession(WebSocketSession session){
        return sessions.add(session);
    }

    public void addMessage(SendChatEvent event){
        Chat chat = chatMap.get(event.getAddress());
        if(chat == null){
            chat = new Chat();
            chatMap.put(event.getAddress(), chat);
        }
        chat.addMessage(event.getMessage(), event.getSession());
    }

    public void joinToChat(JoinChatEvent event){
        Chat chat = chatMap.get(event.getAddress());
        if(chat == null){
            chat = new Chat();
            chatMap.put(event.getAddress(), chat);
        }
        User user = users.get(event.getSession().getId());
        user.joinChat(chat);
        chat.joinChat(event.getSession());
    }


    public HashSet<WebSocketSession> getSessions() {
        return sessions;
    }

    public boolean addUser(WebSocketSession session){
        if(users.containsKey(session.getId())){
            return false;
        }
        users.put(session.getId(), new User(session));
        return true;
    }

    public void removeUser(WebSocketSession session){
        User user = users.get(session.getId());
        user.removeFromChats();
        users.remove(session.getId());
        //?
    }
}
