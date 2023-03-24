package com.example.springChat.handler;

import com.example.springChat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Configuration
@Component
public class Task {
    @Autowired
    private ChatService chatService;

    @Scheduled(fixedDelay = 100)
    public void scheduleFixedDelayTask() {
        //chatService.updateChats();
        //chatService.sendMessages();
    }


    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() {
        chatService.loadChatsFromDataBase();
        // do whatever you need here
    }
}

