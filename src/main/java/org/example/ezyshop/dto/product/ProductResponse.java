package org.example.ezyshop.dto.product;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.example.ezyshop.base.BaseResponse;
import org.example.ezyshop.dto.pagination.PageDto;

import java.util.List;

@NoArgsConstructor
@Data
@Accessors(chain = true)
public class ProductResponse extends BaseResponse {

    private List<ProductDTO> content;
    private PageDto pageDto;

    public ProductResponse(boolean success, int code) {
        super(success, code);
    }

    public ProductResponse(boolean success, int code, String message) {
        super(success, code, message);
    }
}
