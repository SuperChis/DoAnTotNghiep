package org.example.ezyshop.dto.auth;

import lombok.Data;
import org.example.ezyshop.base.BaseResponse;

@Data
public class SignUpResponse extends BaseResponse {
    public SignUpResponse(boolean success, int code, String mess) {
        super(success, code, mess);
    }
    public SignUpResponse(boolean success, int code) {
        super(success, code);
    }
}
