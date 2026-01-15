package com.osen.osenshop.auth.domain.services;

import com.osen.osenshop.auth.domain.models.User;
import com.osen.osenshop.auth.application.dtos.RegisterRequest;
import com.osen.osenshop.auth.application.dtos.LoginRequest;

import java.util.Map;

public interface AuthService {
    Map<String, String> login(LoginRequest loginRequestDTO) throws Exception;
    boolean validateToken(String token);
    String getUserFromToken(String token);
    void createUser(RegisterRequest createUserDto);
    User getUser(Long id);
    Map<String, String> refreshToken(String refreshToken);
}
