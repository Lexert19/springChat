package com.example.springChat.repository;

import com.example.springChat.model.Chat;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ChatRepository extends R2dbcRepository<Chat, Integer> {
    //Chat findById(int id);
}
