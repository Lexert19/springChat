package com.example.springChat.handler;

import com.example.springChat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class Task {
    @Autowired
    private ChatService chatService;

    @Scheduled(fixedDelay = 100)
    public void scheduleFixedDelayTask() {
        chatService.updateChats();
        //chatService.sendMessages();
    }
}
