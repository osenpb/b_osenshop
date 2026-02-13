package com.osen.osenshop.auth.controllers;

import com.osen.osenshop.auth.application.dtos.*;
import com.osen.osenshop.auth.application.mappers.AuthMapper;
import com.osen.osenshop.auth.domain.models.User;
import com.osen.osenshop.auth.domain.services.AuthService;
import com.osen.osenshop.auth.domain.services.CookieService;
import com.osen.osenshop.auth.domain.services.TokenService;
import com.osen.osenshop.auth.domain.services.UserService;
import com.osen.osenshop.common.handler_exception.exceptions.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth") //
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final UserService userService;
    private final CookieService cookieService;
    private final TokenService tokenService;


    public AuthController(AuthService authService, UserService userService, CookieService cookieService, TokenService tokenService) {
        this.authService = authService;
        this.userService = userService;
        this.cookieService = cookieService;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody @Valid RegisterRequest createUserDto) {
        authService.createUser(createUserDto);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @RequestBody @Valid LoginRequest loginRequestDTO,
            HttpServletResponse response) throws Exception {

        // 1. Lógica de negocio: generar tokens
        Map<String, String> tokens = authService.login(loginRequestDTO);

        // 2. Delegar la gestión de cookies (Uso de CookieService)
        cookieService.addTokenCookies(response, tokens);

        // 3. Obtener datos del usuario
        User user = userService.findByEmail(loginRequestDTO.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

        return ResponseEntity.ok(AuthMapper.toDto(user));
    }


    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(
            @CookieValue(name = "refresh_token", required = false) String refreshToken,
            HttpServletResponse response) {

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // genero nuevos tokens de rotacion
        Map<String, String> tokens = tokenService.refreshToken(refreshToken);

        // 3. Sobrescribir cookies
        cookieService.addTokenCookies(response, tokens);
        log.info("Nuevos tokens generados: " + tokens);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        cookieService.clearTokenCookies(response);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal User user) {
        User me = userService.findByEmail(user.getEmail()).orElseThrow(() -> new EntityNotFoundException("User not found with email " + user.getEmail()));
        log.info("Usuario encontrado con nombre: " + me.getFirstName());
        UserResponse userResponse = AuthMapper.toDto(me);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/check-status")
    public ResponseEntity<AuthResponse> checkStatus(
            @AuthenticationPrincipal User user,
            HttpServletRequest request) {

        if (user == null || user.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User myUser = userService.findByEmail(user.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found with email " + user.getEmail()));
        UserResponse userResponse = AuthMapper.toDto(myUser);

        // El token viene en la cookie, no en Authorization header
        // Si necesitas devolverlo, valida que exista primero
        String token = "";
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        AuthResponse authResponse = new AuthResponse(
                userResponse,
                Map.of("accessToken", token,
                        "refreshToken", "")
        );
        return ResponseEntity.ok(authResponse);
    }

}
