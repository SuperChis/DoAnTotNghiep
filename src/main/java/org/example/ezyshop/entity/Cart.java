package org.example.ezyshop.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.ezyshop.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Cart extends BaseEntity {

    private Double totalPrice = 0.0;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart",
            cascade = { CascadeType.PERSIST, CascadeType.MERGE },
            orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    private boolean isDeleted;
}
