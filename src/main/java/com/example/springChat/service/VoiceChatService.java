package com.example.springChat.service;

import com.example.springChat.element.event.SendAudioChatEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Mono;

@Service
public class VoiceChatService {


    public void getAudio(SendAudioChatEvent event){
        WebSocketMessage m = new WebSocketMessage(WebSocketMessage.Type.BINARY, event.getAudio());
        m.retain();
        event.getSession().send(Mono.just(m)).subscribe();
    }
}
