package org.example.ezyshop.dto.store;

import lombok.Data;
import org.example.ezyshop.base.BaseResponse;

@Data
public class StoreResponse extends BaseResponse {
    public StoreResponse() {
    }

    public StoreResponse(boolean success, int code) {
        super(success, code);
    }

    public StoreResponse(boolean success, int code, String message) {
        super(success, code, message);
    }
}
