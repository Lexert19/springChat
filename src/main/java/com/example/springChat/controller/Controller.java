package com.example.springChat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class Controller {
    @GetMapping("/test")
    public Mono<ResponseEntity<Long>> test(){
        long number = 0;
        for(long i=0; i<50000000000l; i++){
            number+=1;
        }
        return Mono.just(ResponseEntity.ok(number));
    }
}
