package org.example.ezyshop.dto.auth;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private Long userId;
    private String newPassword;
    private String oldPassword;
}
