package org.example.ezyshop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import org.example.ezyshop.base.BaseEntity;

@Entity
public class Shipment extends BaseEntity {

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "price",nullable = false)
    private double price;

    @Column(name = "status", nullable = true)
    private boolean status;

    @PrePersist
    void createdAt() {
        this.status = true;
    }
}
