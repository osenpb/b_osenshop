package com.osen.osenshop.auth.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Obtener orígenes permitidos de variable de entorno
        String allowedOriginsEnv = System.getenv("ALLOWED_ORIGINS");
        List<String> allowedOrigins = allowedOriginsEnv != null
            ? Arrays.asList(allowedOriginsEnv.split(","))
            : Arrays.asList(
                "http://localhost:4200",
                "http://localhost:5173"  // Vite default
            );

        configuration.setAllowedOrigins(allowedOrigins);

        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));

        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "X-XSRF-TOKEN",
                "XSRF-TOKEN"
        ));

        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Set-Cookie"
        ));

        configuration.setAllowCredentials(true);

        // Cuánto tiempo el navegador cachea la respuesta preflight
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
