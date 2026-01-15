package com.osen.osenshop.auth.application.mappers;

import com.osen.osenshop.auth.domain.models.User;
import com.osen.osenshop.auth.application.dtos.RegisterRequest;
import com.osen.osenshop.auth.application.dtos.LoginRequest;
import com.osen.osenshop.auth.application.dtos.UserResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class AuthMapper {
    private AuthMapper() {
        throw new UnsupportedOperationException("This class should never be instantiated");
    }

    public static User fromDto(final RegisterRequest registerRequest) {
        return User.builder()
                .email(registerRequest.email())
                .firstName(registerRequest.firstName())
                .lastName(registerRequest.lastName())
                .build();
    }

    public static Authentication fromDto(final LoginRequest loginRequestDTO) {
        return new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.password());
        // en este caso como username estoy usando el email

    }

    public static UserResponse toDto(final User user) {
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole());
    }
}
