package com.example.springChat.controller;

import com.example.springChat.model.User;
import com.example.springChat.repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class SearchController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Gson gson;

    @GetMapping("/searchUserId")
    public Mono<ResponseEntity<String>> searchUserId(@RequestParam("name") String name){
        Mono<ResponseEntity<String>> response = userRepository.findByName(name)
                .map(user ->{
                    return ResponseEntity.ok(gson.toJson(user.getId()));
                });
        return response;
    }

    @GetMapping("/searchUser")
    public Mono<ResponseEntity<String>> searchUser(@RequestParam("name") String name){
        Mono<ResponseEntity<String>> response = userRepository.findByNameContains(name).collectList()
                .map(user ->{
                    return ResponseEntity.ok(gson.toJson(user, new TypeToken<List<User>>(){}.getType()));
                });
        return response;
    }
}
