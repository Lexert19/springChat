package com.example.springChat.service;

import com.example.springChat.config.JwtSigner;
import com.example.springChat.element.event.*;
import com.example.springChat.model.Chat;
import com.example.springChat.element.Message;
import com.example.springChat.model.ChatAccess;
import com.example.springChat.model.User;
import com.example.springChat.repository.ChatRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatService {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ChatRepositoryService chatRepositoryService;
    @Autowired
    private JwtSigner jwtSigner;

    private Map<Integer, Chat> chatMap;
    private Map<String, User> users;

    public ChatService() {
        chatMap = new ConcurrentHashMap<>();
        users = new ConcurrentHashMap<>();
    }

    public void updateChatRequest(UpdateChatEvent event){
        Chat chat = chatMap.get(event.getAddress());
        User user = users.get(event.getSession().getId());
        chat.addChatRequest(event, user.getId());
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
        User user = users.get(event.getSession().getId());
        Chat chat = chatMap.get(event.getAddress());
        if(chat == null){
            return;
        }
        chat.addMessage(event.getMessage(), user.getId());
    }

    public void addUserToChat(AddUserToChatEvent event){
        User sender = users.get(event.getSession().getId());
        User user = new User();
        user.setId(event.getUserId());
        Chat chat = chatMap.get(event.getAddress());
        if(chat == null){
            return;
        }
        chat.addUser(sender, user, ChatAccess.Role.USER);
    }

    public void loginToChat(LoginChatEvent event){
        //authorize
        Jws<Claims> claimsJws = jwtSigner.validateJwt(event.getToken());
        User user = users.get(event.getSession().getId());
        user.setSession(event.getSession());
        user.setName(claimsJws.getBody().getSubject());
        user.setId(claimsJws.getBody().get("userId", Double.class).intValue());
    }

    public void joinToChat(JoinChatEvent event){
        User user = users.get(event.getSession().getId());
        Chat chat = chatMap.get(event.getAddress());
        if(chat == null){
            return;
        }
        //User user = users.get(event.getSession().getId());
        if(chat.joinChat(user)){
            user.joinChat(chat);
        }
        /*chat.joinChat(user);
        chat.joinChat(event.getSession());*/
    }

    public boolean createChat(CreateChatEvent event){
        User user = users.get(event.getSession().getId());
        Chat chat = chatMap.get(event.getAddress());
        if(chat == null){
            chat = new Chat(user);
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
    }

    public void loadChatsFromDataBase(){
        Flux<Chat> chats = chatRepository.findAll();
        chats.collectList().flatMap(chatList ->{
            for(Chat chat : chatList){
                chat.convertJsonMessagesToMessages();
                chat.convertJsonUsersToUsers();
                chatMap.put(chat.getId(), chat);
            }
            return Mono.empty();
        }).subscribe();
    }

    public Mono<Void> saveChatIntoDataBase(SaveChatEvent event){
        Chat chat = chatMap.get(event.getAddress());
        chat.convertMessagesToJsonMessages();
        chat.convertUsersToJsonUsers();
        return chatRepository.save(chat).then();
    }
}
