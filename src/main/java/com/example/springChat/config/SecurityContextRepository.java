package com.example.springChat.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtSigner jwtSigner;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return null;
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        Jws<Claims> claimsJws;

        String token = "";
        token = exchange.getRequest().getHeaders().getFirst("token");
        if(token == null){
            token=exchange.getRequest().getHeaders().getFirst("Sec-WebSocket-Protocol");
        }

        try{
            claimsJws = jwtSigner.validateJwt(token);
        }catch (Exception e){
            Authentication authentication = new UsernamePasswordAuthenticationToken("","");
            return Mono.just(new SecurityContextImpl(authentication));
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "",
                "",
                Collections.singleton(new SimpleGrantedAuthority(claimsJws.getBody().get("role", String.class)))
                );

        return Mono.just(new SecurityContextImpl(authentication));
    }
}
