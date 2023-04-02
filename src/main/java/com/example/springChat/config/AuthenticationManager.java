package com.example.springChat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {
    @Autowired
    private JwtSigner jwtSigner;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication).map(e -> {
            return jwtSigner.validateJwt((String) e.getCredentials());
        }).onErrorResume(e -> {
            return Mono.empty();
        }).map(jwt -> {
            return new UsernamePasswordAuthenticationToken(
                    jwt.getBody().getSubject(),
                    authentication.getCredentials(),
                    Collections.singleton(new SimpleGrantedAuthority(jwt.getBody().get("role", String.class)))
            );
        });
    }
}
