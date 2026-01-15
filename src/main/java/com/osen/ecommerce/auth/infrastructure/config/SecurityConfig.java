package com.osen.ecommerce.auth.infrastructure.config;

import com.osen.ecommerce.auth.infrastructure.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    String API_VERSION = "/api/v1";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(String.format("%s/auth/**", API_VERSION)).permitAll()
                        .requestMatchers(String.format("%s/admin/**", API_VERSION)).hasRole("ADMIN") //hasRole("ADMIN")
                        .requestMatchers(String.format("%s/users/**", API_VERSION)).authenticated() //hasRole("ADMIN")
                        .requestMatchers(String.format("%s/cart/**", API_VERSION)).hasRole("USER")
                        .requestMatchers(String.format("%s/orders/**", API_VERSION)).authenticated()
                        .requestMatchers(String.format("%s/categories/**", API_VERSION)).authenticated()
                        .requestMatchers(String.format("%s/products/**", API_VERSION)).permitAll()
                        .anyRequest().authenticated()
                )//registra el filtro ANTES del de login por formulario
//                .oauth2Login(Customizer.withDefaults()) // para registro OAuth2
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }



}

