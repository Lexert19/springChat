package com.example.springChat.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
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
        try{
            String token = exchange.getRequest().getHeaders().getFirst("token");
            if(token == null){
                token=exchange.getRequest().getHeaders().getFirst("Sec-WebSocket-Protocol");
            }
            Jws<Claims> claimsJws = jwtSigner.validateJwt(token);

        }catch (Exception e){
            Authentication authentication = new UsernamePasswordAuthenticationToken("token", "token");
            return authenticationManager.authenticate(authentication).map(auth -> {
                return new SecurityContextImpl(authentication);
            });
        }


        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        authorities.add(authority);

        Authentication auth = new UsernamePasswordAuthenticationToken("", "", authorities);
        return authenticationManager.authenticate(auth).map(authentication -> {
            return new SecurityContextImpl(auth);
        });
    }
}
