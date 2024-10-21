package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for APIs; enable with proper configuration in production
            .csrf(csrf -> csrf.disable())
            
            // Configure CORS
            .cors(Customizer.withDefaults())
            
            // Authorize requests
            .authorizeHttpRequests(authz -> authz
                // Permit all HTTP methods to /api/** endpoints
                .requestMatchers("/api/**").permitAll()
                
                // Any other request must be authenticated
                .anyRequest().authenticated()
            )
            
            // HTTP Basic Authentication
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}