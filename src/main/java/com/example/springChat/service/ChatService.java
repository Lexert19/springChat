package com.example.springChat.service;

import com.example.springChat.element.event.*;
import com.example.springChat.model.Chat;
import com.example.springChat.element.Message;
import com.example.springChat.model.User;
import com.example.springChat.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.*;

public class ChatService {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ChatRepositoryService chatRepositoryService;

    private Map<Integer, Chat> chatMap;
    private Map<String, User> users;

    public ChatService() {
        chatMap = Collections.synchronizedMap(new HashMap<>());
        users = Collections.synchronizedMap(new HashMap<>());
    }

    public void updateChatRequest(UpdateChatEvent event){
        Chat chat = chatMap.get(event.getAddress());
        chat.addChatRequest(event);
    }

    public void updateChats(){
        for(Map.Entry<Integer, Chat> chat : chatMap.entrySet()){
            for(UpdateChatEvent event : chat.getValue().getUpdateChatRequests()){
                WebSocketSession session = event.getSession();
                try{
                    for(Message message : chat.getValue().getMessages()){
                        session.send(Mono.just(session.textMessage(message.getMessage()))).subscribe();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            chat.getValue().clearChatRequests();
        }
    }

    public void addMessage(SendChatEvent event){
        Chat chat = chatMap.get(event.getAddress());
        if(chat == null){
            return;
        }
        chat.addMessage(event.getMessage(), event.getSession());
    }

    public void joinToChat(JoinChatEvent event){
        Chat chat = chatMap.get(event.getAddress());
        if(chat == null){
            return;
        }
        User user = users.get(event.getSession().getId());
        user.joinChat(chat);
        chat.joinChat(event.getSession());
    }

    public boolean createChat(CreateChatEvent event){
        Chat chat = chatMap.get(event.getAddress());
        if(chat == null){
            chat = new Chat();
            chatMap.put(event.getAddress(), chat);
            //chatRepositoryService.addChat(chat);
        }
        return true;
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

    public Mono<Void> saveChatIntoDataBase(SaveChatEvent event){
        return chatRepositoryService.saveChatIntoRepository(chatMap.get(event.getAddress()));
    }
}
