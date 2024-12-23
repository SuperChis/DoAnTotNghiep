package org.example.ezyshop.dto.size;

import lombok.Data;
import lombok.experimental.Accessors;
import org.example.ezyshop.base.BaseResponse;

import java.util.List;

@Data
@Accessors(chain = true)
public class SizeResponse extends BaseResponse {
    private SizeDTO dto;
    private List<SizeDTO> sizeList;

    public SizeResponse() {
    }

    public SizeResponse(boolean success, int code) {
        super(success, code);
    }

    public SizeResponse(boolean success, int code, String message) {
        super(success, code, message);
    }
}
