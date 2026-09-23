package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desativa o CSRF para facilitar os testes das rotas REST
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())) // Permite o H2 Console se necessário
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Libera todas as rotas publicamente para o e-commerce local
            );
        
        return http.build();
    }
}