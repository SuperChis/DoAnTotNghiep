package org.example.ezyshop.dto.category;

import lombok.Data;
import lombok.experimental.Accessors;
import org.example.ezyshop.base.BaseResponse;
import org.example.ezyshop.dto.pagination.PageDto;

import java.util.List;

@Data
@Accessors(chain = true)
public class CategoryResponse extends BaseResponse {

    private CategoryDTO dto;
    private List<CategoryDTO> dtoList;
    private PageDto pageDto;

    public CategoryResponse() {
    }

    public CategoryResponse(boolean success, int code) {
        super(success, code);
    }

    public CategoryResponse(boolean success, int code, String message) {
        super(success, code, message);
    }
}
