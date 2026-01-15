package com.osen.osenshop.auth.application.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,


        @Email(message = "El correo debe ser unico")
        @NotBlank
        String email,

        @NotBlank
        String password

) {
}
