package org.example.ezyshop.dto.store;

import lombok.Data;
import lombok.experimental.Accessors;
import org.example.ezyshop.base.BaseResponse;

@Data
@Accessors(chain = true)
public class StoreRequestState extends BaseResponse {
    private boolean isRequesting;
    private boolean isApproved;

    public StoreRequestState() {
    }

    public StoreRequestState(boolean success, int code) {
        super(success, code);
    }

    public StoreRequestState(boolean success, int code, String message) {
        super(success, code, message);
    }
}
