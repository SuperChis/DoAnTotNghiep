package org.example.ezyshop.entity;

import jakarta.persistence.*;
import org.example.ezyshop.base.BaseEntity;

@Entity
public class SizeEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private Variant variant;

    @Column(name = "size", nullable = false)
    private String size;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "price", nullable = false)
    private double price;

    private String imageUrl;
}
