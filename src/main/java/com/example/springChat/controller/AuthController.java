package com.example.springChat.controller;

import com.example.springChat.config.JwtSigner;
import com.example.springChat.model.User;
import com.example.springChat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JwtSigner jwtSigner;

    @GetMapping("/auth/register")
    public Mono<ResponseEntity<String>> register(@RequestParam(name="name") String name,
                                            @RequestParam(name = "password") String password,
                                            @RequestParam(name = "email") String email){

       Mono<ResponseEntity<String>> response = userRepository.findByNameOrEmail(name, email)
               .map(user -> {
                   if(user.getName() == name)
                       return ResponseEntity.ok("Imie jest zajęte");
                   if(user.getEmail() == email)
                       return ResponseEntity.ok("Email jest zajęty");
                   return ResponseEntity.ok("Email jest zajęty");
               })
               .switchIfEmpty(Mono.defer(()->{
                   User user = new User();
                   user.setName(name);
                   user.setEmail(email);
                   user.setPassword(bCryptPasswordEncoder.encode(password));
                   return userRepository.save(user)
                           .thenReturn(ResponseEntity.ok("Zarejestrowano pomyślnie"));
               }));
       return response;
    }

    @GetMapping("/auth/login")
    public Mono<ResponseEntity<String>> login(@RequestParam(name = "name") String name,
                                         @RequestParam(name = "password") String password) throws InterruptedException {

        Mono<ResponseEntity<String>> response = userRepository.findByName(name)
                .map(user ->{
                    if(!bCryptPasswordEncoder.matches(password, user.getPassword()))
                        return ResponseEntity.ok("Nie prawidłowe hasło");

                    String jwt = jwtSigner.createJwt(name, user.getId());
                    return ResponseEntity.ok(jwt);
                })
                .switchIfEmpty(Mono.defer(()->{
                    return Mono.just(ResponseEntity.ok("Użytkownik nie istnieje"));
                }));

        return response;
    }
}
