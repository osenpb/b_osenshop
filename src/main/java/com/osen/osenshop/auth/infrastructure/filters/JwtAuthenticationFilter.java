package com.osen.osenshop.auth.infrastructure.filters;

import com.osen.osenshop.auth.domain.services.AuthService;
import com.osen.osenshop.common.handler_exception.exceptions.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(AuthService authService, UserDetailsService userDetailsService) {
        this.authService = authService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException, TokenExpiredException {
        

        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }


    try {

        final Optional<String> token = resolveTokenFromHeader(request);

            // Si no hay token o es inválido, que Spring Security maneje la autorización
            if (token.isEmpty() || !authService.validateToken(token.get())) {
                filterChain.doFilter(request, response);
                return;
            }


        String userName = authService.getUserFromToken(token.get());

        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

        authenticationToken.setDetails(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    } catch (TokenExpiredException e) {
        log.warn("Token expirado: {}", e.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expirado");
        return;

    }catch (Exception e) {
        log.error("Error autenticando JWT", e);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
        return;
    }
        filterChain.doFilter(request, response);
    }

    //si en lugar de gestionar mediante cookies quieres hacerlo con authorization, solo cambias este metodo
    private Optional<String> resolveTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return Optional.of(bearerToken.substring(7)); // quitar "Bearer "
        }
        return Optional.empty();
    }
}
