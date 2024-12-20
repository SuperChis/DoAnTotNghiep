package org.example.ezyshop.dto.store;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.example.ezyshop.base.BaseResponse;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class StoreResponse extends BaseResponse {
    private StoreDTO dto;
    private List<StoreDTO> dtoList;

    public StoreResponse(boolean success, int code) {
        super(success, code);
    }

    public StoreResponse(boolean success, int code, String message) {
        super(success, code, message);
    }
}
