package org.example.ezyshop.dto.review;

import lombok.Data;
import org.example.ezyshop.base.BaseResponse;

import java.util.List;

@Data
public class ReviewResponse extends BaseResponse {
    private ReviewDTO reviewDTO;
    private List<ReviewDTO> reviewDTOS;

    public ReviewResponse() {
    }

    public ReviewResponse(boolean success, int code) {
        super(success, code);
    }

    public ReviewResponse(boolean success, int code, String message) {
        super(success, code, message);
    }
}
