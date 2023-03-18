package com.example.springChat.service;

import com.example.springChat.model.Chat;
import com.example.springChat.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ChatRepositoryService {
    @Autowired
    private ChatRepository chatRepository;

    public Mono<Void> saveChatIntoRepository(Chat chat){
        chat.updateRawChat();
        return chatRepository.save(chat).then();
    }

}


