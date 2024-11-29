package org.example.ezyshop.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInRequest {

    @NotBlank(message = "Email is empty")
    private String email;

    @NotBlank(message = "Password is empty")
    private String password;

}
