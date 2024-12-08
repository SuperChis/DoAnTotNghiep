package org.example.ezyshop.dto.auth;

import lombok.Data;
import lombok.experimental.Accessors;
import org.example.ezyshop.base.BaseResponse;
import org.example.ezyshop.entity.Role;

import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)
public class SignUpResponse extends BaseResponse {

    private String token;
    private String type = "Bearer";
    private Set<Role> roles;
    private String refreshToken;

    public SignUpResponse(boolean success, int code, String mess) {
        super(success, code, mess);
    }
    public SignUpResponse(boolean success, int code) {
        super(success, code);
    }
}
