package org.example.ezyshop.dto.shipment;

import lombok.Data;
import lombok.experimental.Accessors;
import org.example.ezyshop.base.BaseResponse;
import org.example.ezyshop.dto.pagination.PageDto;

import java.util.List;

@Data
@Accessors(chain = true)
public class ShipmentResponse extends BaseResponse {
    private ShipmentDTO dto;
    private List<ShipmentDTO> dtoList;
    private PageDto pageDto;

    public ShipmentResponse() {
    }

    public ShipmentResponse(boolean success, int code) {
        super(success, code);
    }

    public ShipmentResponse(boolean success, int code, String message) {
        super(success, code, message);
    }
}
