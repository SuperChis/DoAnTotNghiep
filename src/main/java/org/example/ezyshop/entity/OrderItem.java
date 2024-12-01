package org.example.ezyshop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ezyshop.base.BaseEntity;

@Entity
@Data
@Table(name = "order_items")
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem extends BaseEntity {

    private Integer quantity;

    private double discount;

    private double orderedProductPrice;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
