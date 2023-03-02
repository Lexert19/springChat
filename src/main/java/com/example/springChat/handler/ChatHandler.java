package com.example.springChat.handler;


import com.example.springChat.model.event.JoinChatEvent;
import com.example.springChat.model.event.SendChatEvent;
import com.example.springChat.model.event.UpdateChatEvent;
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
            return session.send(session.receive().doFinally(msg -> {
                chatService.removeUser(session);
                session.close();

            }).map(msg -> {
                parseToEvent(msg.getPayloadAsText(), session);
                return "";
            }).map(session::textMessage));
        }
        return Mono.empty();




       /* return session.send(session.receive()
                .map(msg -> {
                    //String message = msg.getPayloadAsText();
                    parseToEvent(msg.getPayloadAsText(), session);
                    return "";
                })
                .map(session::textMessage));*/
        //return  session.send(Mono.just(session.textMessage(": ")));
    }

    public void parseToEvent(String message, WebSocketSession session) {
        if (message.charAt(0) == UpdateChatEvent.id) {
            UpdateChatEvent updateChatEvent = new UpdateChatEvent(message, session);
            chatService.updateChatRequest(updateChatEvent);
        } else if (message.charAt(0) == SendChatEvent.id) {
            SendChatEvent sendChatEvent = new SendChatEvent(message, session);
            chatService.addMessage(sendChatEvent);
        } else if (message.charAt(0) == JoinChatEvent.ID) {
            JoinChatEvent joinChatEvent = new JoinChatEvent(message, session);
            chatService.joinToChat(joinChatEvent);
        }


    }

}
