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
    @Query("SELECT * FROM users WHERE name = :name OR email = :email")
    Mono<User> findByNameOrEmail(String name, String email);


    @Query("SELECT * FROM users WHERE name = :name")
    Mono<User> findByName(String name);
   /* Mono<User> findById(int id);

    Flux<User> findAll();

    Mono<User> findByNameOrEmail(String name, String email);
    Mono<User> findByName(String name);*/
    //Mono<User> findByEmail(String email);
}
