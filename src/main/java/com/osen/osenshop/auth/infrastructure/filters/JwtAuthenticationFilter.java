package com.osen.osenshop.auth.infrastructure.filters;

import com.osen.osenshop.auth.domain.services.AuthService;
import com.osen.osenshop.common.handler_exception.exceptions.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final AuthService authService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(AuthService authService, UserDetailsService userDetailsService) {
        this.authService = authService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Optional<String> tokenOptional = resolveTokenFromCookie(request);

        try {
            if (tokenOptional.isPresent()) {
                String token = tokenOptional.get();

                // Solo procesamos si el token es válido y no hay autenticación previa
                if (authService.validateToken(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
                    String userName = authService.getUserFromToken(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (TokenExpiredException e) {
            log.warn("Token expirado: {}", e.getMessage());
            // Opcional: No envíes error aquí si quieres que permitAll funcione
            // Solo limpia el contexto si es necesario
        } catch (Exception e) {
            log.error("Error procesando JWT en el filtro", e);
            // No bloqueamos la cadena de filtros, dejamos que la seguridad decida después
        }
        filterChain.doFilter(request, response);
    }

    //si en lugar de gestionar mediante cookies quieres hacerlo con authorization, solo cambias este metodo
    private Optional<String> resolveTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return Optional.empty();

        return java.util.Arrays.stream(request.getCookies())
                .filter(cookie -> "access_token".equals(cookie.getName()))
                .map(jakarta.servlet.http.Cookie::getValue)
                .findFirst();
    }
}
