package com.osen.osenshop.auth.controllers;

import com.osen.osenshop.auth.application.dtos.AuthResponse;
import com.osen.osenshop.auth.application.dtos.UserResponse;
import com.osen.osenshop.auth.application.mappers.AuthMapper;
import com.osen.osenshop.auth.domain.models.User;
import com.osen.osenshop.auth.domain.services.AuthService;
import com.osen.osenshop.auth.application.dtos.RegisterRequest;
import com.osen.osenshop.auth.application.dtos.LoginRequest;
import com.osen.osenshop.auth.domain.services.UserService;
import com.osen.osenshop.common.handler_exception.exceptions.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth") //
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody @Valid RegisterRequest createUserDto) {
        authService.createUser(createUserDto);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest loginRequestDTO) throws Exception {

        log.info("Email recibido: {}", loginRequestDTO.email());

        Map<String, String> tokens = authService.login(loginRequestDTO);

        User user = userService.findByEmail(loginRequestDTO.email())
                .orElseThrow(() ->
                        new BadCredentialsException("Credenciales inválidas"));
        
        UserResponse userResponse = AuthMapper.toDto(user);

        AuthResponse authResponse = new AuthResponse(userResponse, tokens);

        return ResponseEntity.ok(authResponse);

    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> request){
        String refreshToken = request.get("refresh");
        Map<String, String> newAccessToken = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(newAccessToken);

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

        // cuando la primera vez se chequea la existencia de un token
        if (user == null || user.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User myUser = userService.findByEmail(user.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found with email " + user.getEmail()));
        UserResponse userResponse = AuthMapper.toDto(myUser);

        String token = request.getHeader("Authorization")
                .substring(7);
        log.info("token encontrado {}", token);
        AuthResponse authResponse = new AuthResponse(
                userResponse,
                Map.of("accessToken", token,
                        "refreshToken", "asd")
        );
        return ResponseEntity.ok(authResponse);
    }

}
