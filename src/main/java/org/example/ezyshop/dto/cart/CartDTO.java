package org.example.ezyshop.dto.cart;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class CartDTO {
    private Long id;
    private Double totalPrice;
    private List<CartItemDTO> cartItemDTOS;
}
