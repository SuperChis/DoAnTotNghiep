package org.example.ezyshop.dto.category;

import org.example.ezyshop.base.BaseResponse;

public class CategoryResponse extends BaseResponse {
    public CategoryResponse() {
    }

    public CategoryResponse(boolean success, int code) {
        super(success, code);
    }

    public CategoryResponse(boolean success, int code, String message) {
        super(success, code, message);
    }
}
