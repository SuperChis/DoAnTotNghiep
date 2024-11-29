package org.example.ezyshop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private List<String> roles;
    private String refreshToken;

    public JwtResponse(String accessToken, String refreshToken, List<String> roles) {
        this.token = accessToken;
        this.roles = roles;
        this.refreshToken = refreshToken;
    }
}
