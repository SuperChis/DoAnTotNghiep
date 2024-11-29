package org.example.ezyshop.dto.refreshToken;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RefreshTokenRequest {

    @NotBlank
    private String refreshToken;

}
