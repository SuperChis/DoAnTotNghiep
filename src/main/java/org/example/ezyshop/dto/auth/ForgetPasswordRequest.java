package org.example.ezyshop.dto.auth;

import lombok.Data;

@Data
public class ForgetPasswordRequest {
    private String email;
    private String password;
}
