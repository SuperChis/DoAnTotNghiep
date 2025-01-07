package org.example.ezyshop.dto.order;

import lombok.Data;
import lombok.experimental.Accessors;
import org.example.ezyshop.base.BaseResponse;
import org.example.ezyshop.dto.pagination.PageDto;
import org.example.ezyshop.dto.shipment.ShipmentDTO;
import org.example.ezyshop.entity.Shipment;

import java.util.List;

@Data
@Accessors(chain = true)
public class OrderResponse extends BaseResponse {
    private OrderDTO order;
    private List<OrderDTO> dtoList;
    private ShipmentDTO shipment;
    private PageDto pageDto;

    public OrderResponse() {
    }

    public OrderResponse(boolean success, int code) {
        super(success, code);
    }

    public OrderResponse(boolean success, int code, String message) {
        super(success, code, message);
    }
}

