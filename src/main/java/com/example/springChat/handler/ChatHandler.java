package com.example.springChat.handler;


import com.example.springChat.element.event.*;
import com.example.springChat.service.ChatService;
import com.example.springChat.service.VoiceChatService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;


public class ChatHandler implements WebSocketHandler {
    @Autowired
    private ChatService chatService;
    @Autowired
    private VoiceChatService voiceChatService;

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        if (chatService.addUser(session)) {
            return session.send(e -> session.textMessage(""))
                    .and(session.receive().doFinally(msg ->{
                        chatService.removeUser(session);
                        session.close();

                    })
                    .map(msg -> {
                        return parseToEvent(msg, session);
                    }).flatMap(e -> e));
        }
        return Mono.empty();
    }

    public Mono<Void> parseToEvent(WebSocketMessage webSocketMessage, WebSocketSession session) {
        String message = webSocketMessage.getPayloadAsText();
        DataBuffer bytes = webSocketMessage.getPayload();
        char id = (char)bytes.getByte(0);

        //char id = message.charAt(0);
        if (id == UpdateChatEvent.id) {
            UpdateChatEvent updateChatEvent = new UpdateChatEvent(message, session);
            chatService.updateChat(updateChatEvent);

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

        }else if(id == SendAudioChatEvent.ID){
            SendAudioChatEvent sendAudioChatEvent = new SendAudioChatEvent(bytes, session);
            //voiceChatService.getAudio(sendAudioChatEvent);
            chatService.sendAudio(sendAudioChatEvent);

        }else if(id == JoinVoiceChatEvent.ID){
            JoinVoiceChatEvent joinVoiceChatEvent = new JoinVoiceChatEvent(message, session);
            chatService.joinVoiceChat(joinVoiceChatEvent);

        }else if(id == LeaveVoiceChatEvent.ID){
            LeaveVoiceChatEvent leaveVoiceChatEvent = new LeaveVoiceChatEvent(message, session);
            chatService.leaveVoiceChat(leaveVoiceChatEvent);

        }
        return Mono.empty();

    }

}
