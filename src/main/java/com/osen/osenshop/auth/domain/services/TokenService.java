package com.osen.osenshop.auth.domain.services;

import com.osen.osenshop.auth.domain.models.User;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface TokenService {
    String generateToken(Authentication authentication);
    String getUserFromToken(String token);
    boolean validateToken(String token);
    String generateRefreshToken(User user);

    Map<String, String> refreshToken(String refreshToken);

}

