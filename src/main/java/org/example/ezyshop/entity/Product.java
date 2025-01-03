package org.example.ezyshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minidev.json.annotate.JsonIgnore;
import org.example.ezyshop.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

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
    private double originalPrice;

    @Column()
    private double discount;

    @Column()
    private double specialPrice;

    @Column()
    private String description;

    @Column()
    private String summary;

    @Column(name = "image")
    private String imageURL;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product",
            cascade = { CascadeType.PERSIST, CascadeType.MERGE },
            fetch = FetchType.LAZY)
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Variant> variants = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    private boolean isDeleted;
}

