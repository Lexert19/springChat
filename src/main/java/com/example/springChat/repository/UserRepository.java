package com.example.springChat.repository;

import com.example.springChat.model.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, Integer> {
    @Query("SELECT * FROM users WHERE name = :name OR email = :email LIMIT 1")
    Mono<User> findByNameOrEmail(String name, String email);


    @Query("SELECT * FROM users WHERE name = :name")
    Mono<User> findByName(String name);


    @Query("SELECT id, name FROM users WHERE name LIKE concat('%',:name,'%')")
    Flux<User> findByNameContains(String name);
}
