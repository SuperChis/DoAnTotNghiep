package org.example.ezyshop.dto.cart;

import lombok.Data;
import lombok.experimental.Accessors;
import org.example.ezyshop.base.BaseResponse;
import org.example.ezyshop.dto.pagination.PageDto;

@Data
@Accessors(chain = true)
public class CartResponse extends BaseResponse {
    private CartDTO dto;
    private PageDto pageDto;

    public CartResponse(CartDTO dto) {
        this.dto = dto;
    }

    public CartResponse() {
    }

    public CartResponse(boolean success, int code) {
        super(success, code);
    }

    public CartResponse(boolean success, int code, String message) {
        super(success, code, message);
    }

    public CartResponse(boolean success, int code, CartDTO dto) {
        super(success, code);
        this.dto = dto;
    }

    public CartResponse(boolean success, int code, String message, CartDTO dto) {
        super(success, code, message);
        this.dto = dto;
    }
}
