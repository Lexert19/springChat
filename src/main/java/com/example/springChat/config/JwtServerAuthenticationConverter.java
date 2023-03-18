package com.example.springChat.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {
    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange)
                .flatMap(e ->{
                    return Mono.justOrEmpty(e.getRequest().getCookies());
                })
                .filter(e ->{
                    return !e.isEmpty();
                })
                .map(e ->{
                    return e.get("token");
                })
                .map(e ->{
                    return new UsernamePasswordAuthenticationToken(e, e);
                });
    }
}
