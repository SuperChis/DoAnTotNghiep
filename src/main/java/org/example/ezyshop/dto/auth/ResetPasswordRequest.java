package org.example.ezyshop.dto.auth;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private Long userId;
    private String password;
}
