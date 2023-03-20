package com.example.springChat.handler;


import com.example.springChat.element.event.*;
import com.example.springChat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;


public class ChatHandler implements WebSocketHandler {
    @Autowired
    private ChatService chatService;

    @Override
    public Mono<Void> handle(WebSocketSession session) {


        if (chatService.addUser(session)) {
            return session.send(e -> session.textMessage(""))
                    .and(session.receive().doFinally(msg ->{
                        chatService.removeUser(session);
                        session.close();

                    })
                    .map(msg -> {
                        return parseToEvent(msg.getPayloadAsText(), session);
                    }).flatMap(e -> e));
        }
        return Mono.empty();
    }

    public Mono<Void> parseToEvent(String message, WebSocketSession session) {
        char id = message.charAt(0);
        if (id == UpdateChatEvent.id) {
            UpdateChatEvent updateChatEvent = new UpdateChatEvent(message, session);
            chatService.updateChatRequest(updateChatEvent);

        } else if (id == SendChatEvent.id) {
            SendChatEvent sendChatEvent = new SendChatEvent(message, session);
            chatService.addMessage(sendChatEvent);

        } else if (id == JoinChatEvent.ID) {
            JoinChatEvent joinChatEvent = new JoinChatEvent(message, session);
            chatService.joinToChat(joinChatEvent);

        }else if(id == CreateChatEvent.ID){
            CreateChatEvent createChatEvent = new CreateChatEvent(message, session);
            chatService.createChat(createChatEvent);

        }else if(id == SaveChatEvent.ID){
            SaveChatEvent saveChatEvent = new SaveChatEvent(message, session);
            return chatService.saveChatIntoDataBase(saveChatEvent);

        }else if(id == LoginChatEvent.ID){
            LoginChatEvent loginChatEvent = new LoginChatEvent(message, session);
            chatService.loginToChat(loginChatEvent);
        }else if(id == AddUserToChatEvent.ID){
            AddUserToChatEvent addUserToChatEvent = new AddUserToChatEvent(message, session);
            chatService.addUserToChat(addUserToChatEvent);
        }
        return Mono.empty();

    }

}
