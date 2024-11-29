package org.example.ezyshop.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SignUpRequest {
    @NotNull
    private String username;
    @NotBlank(message = "Email is empty")
    private String email;
    @NotBlank(message = "Password is empty")
    private String password;
    private String branch;
    private String userType;
    private String userLevel;
    private String sex;
}
