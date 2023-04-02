package com.example.springChat.filter;

import com.example.springChat.model.HttpRequestCounter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.HashMap;

@Component
public class HttpRequestCountFilter implements WebFilter {
    HashMap<ByteBuffer, HttpRequestCounter> ipAddresses = new HashMap<>();


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ByteBuffer ip = ByteBuffer.wrap(exchange.getRequest().getRemoteAddress().getAddress().getAddress());
        HttpRequestCounter requestCounter = ipAddresses.get(ip);
        if (requestCounter == null) {
            ipAddresses.put(ip, new HttpRequestCounter());
            return chain.filter(exchange);
        }

        if (requestCounter.validateRequest()) {
            return chain.filter(exchange);
        }
        return Mono.empty();
    }
}
