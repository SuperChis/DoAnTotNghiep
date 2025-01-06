package org.example.ezyshop.dto.order;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private List<Long> cartItemIds;
}
