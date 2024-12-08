package org.example.ezyshop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.example.ezyshop.base.BaseResponse;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class JwtResponse extends BaseResponse {
    private String token;
    private String type = "Bearer";
    private List<String> roles;
    private String refreshToken;

    public JwtResponse(String accessToken, String refreshToken, List<String> roles, boolean success, int code) {
        super(success, code);
        this.token = accessToken;
        this.roles = roles;
        this.refreshToken = refreshToken;
    }

    public JwtResponse(boolean success, int code) {
        super(success, code);
    }

    public JwtResponse(boolean success, int code, String message) {
        super(success, code, message);
    }
}
