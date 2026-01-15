package com.osen.osenshop.auth.application.dtos;

import com.osen.osenshop.auth.domain.models.Role;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Role role
) {
}
