package org.example.ezyshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.example.ezyshop.base.BaseEntity;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Product extends BaseEntity {

    @Column(name = "name",columnDefinition = "nvarchar(100) not null")
    private String name;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price",nullable = false)
    private Double price;

    private String description;

    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<Cart> carts;

    private boolean deleted = false;
}

