package org.example.ezyshop.dto.variant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.example.ezyshop.base.BaseResponse;

import java.util.List;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class VariantResponse extends BaseResponse {
    private VariantDTO dto;
    private List<VariantDTO> dtoList;

    public VariantResponse() {
    }

    public VariantResponse(boolean success, int code) {
        super(success, code);
    }

    public VariantResponse(boolean success, int code, String message) {
        super(success, code, message);
    }
}
