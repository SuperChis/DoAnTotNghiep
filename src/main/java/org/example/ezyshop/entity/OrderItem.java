package org.example.ezyshop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.example.ezyshop.base.BaseEntity;

@Entity
@Data
@Table(name = "order_items")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrderItem extends BaseEntity {

    private Integer quantity;

    private double discount;

    private double price;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Long sizeId;

    private Long variantId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private boolean isDeleted;
}
