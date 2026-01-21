package com.osen.osenshop.auth.application.services;

import com.osen.osenshop.auth.application.mappers.AuthMapper;
import com.osen.osenshop.auth.domain.models.Role;
import com.osen.osenshop.auth.domain.models.User;
import com.osen.osenshop.auth.domain.repository.UserRepository;
import com.osen.osenshop.auth.domain.services.AuthService;
import com.osen.osenshop.auth.domain.services.TokenService;
import com.osen.osenshop.auth.application.dtos.RegisterRequest;
import com.osen.osenshop.auth.application.dtos.LoginRequest;
import com.osen.osenshop.common.handler_exception.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationConfiguration authenticationConfiguration;


    @Override
    public void createUser(RegisterRequest registerRequest) {
        final User createUser = AuthMapper.fromDto(registerRequest);
        createUser.setEmail(registerRequest.email());
        createUser.setPassword(passwordEncoder.encode(registerRequest.password()));
        createUser.setRole(Role.ROLE_USER);
        final User user = userRepository.save(createUser);
        log.info("[USER] : User successfully created with id {}", user.getId());

    }
    @Override
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("[USER] : User not found with id {}", id);
                    return new UsernameNotFoundException("User not found");
                });
    }

    @Override
    public Map<String, String> refreshToken(String refreshToken) {
        return tokenService.refreshToken(refreshToken);
    }

    @Override
    public Map<String, String> login(final LoginRequest loginRequestDTO) throws Exception {

        final AuthenticationManager authenticationManager;

        try {
            authenticationManager = authenticationConfiguration.getAuthenticationManager();
        } catch (Exception e) {
            throw new IllegalStateException("AuthenticationManager no disponible", e);
        }

        Authentication authRequest = AuthMapper.fromDto(loginRequestDTO);
        Authentication authentication =
                authenticationManager.authenticate(authRequest);

        User user = (User) authentication.getPrincipal();

        return Map.of(
                "accessToken", tokenService.generateToken(authentication),
                "refreshToken", tokenService.generateRefreshToken(user)
        );
    }

    @Override
    public boolean validateToken(final String token) throws TokenExpiredException{

            return tokenService.validateToken(token);
    }

    @Override
    public String getUserFromToken(final String token) {
        return tokenService.getUserFromToken(token);
    }


    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.error("[USER] : User not found with email {}", username);
                    return new UsernameNotFoundException("User not found");
                });
    }
}
