package com.example.springChat.config;

import com.example.springChat.filter.HttpRequestNumberFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
//@EnableWebFluxSecurity
//@EnableReactiveMethodSecurity
public class SecurityConfig{
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SecurityContextRepository securityContextRepository;
    @Autowired
    private HttpRequestNumberFilter httpRequestNumberFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        return http.httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .logout().disable()
                .authorizeExchange()
                .pathMatchers("/static/**").permitAll()
                .pathMatchers("/auth/**").permitAll()
                .pathMatchers("/test").permitAll()
                .anyExchange().authenticated()
                .and()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .addFilterAt(httpRequestNumberFilter, SecurityWebFiltersOrder.FIRST)
                .build();
    }




    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
