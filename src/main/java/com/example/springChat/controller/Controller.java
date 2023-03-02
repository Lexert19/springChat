package com.example.springChat.controller;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
public class Controller {

    @GetMapping("/")
    private Mono<ResponseEntity<?>> getMain(){
        return Mono.just(ResponseEntity.ok().body("<h1>text</h1>"));
    }
}
