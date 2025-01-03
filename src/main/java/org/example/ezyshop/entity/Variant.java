package org.example.ezyshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.example.ezyshop.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Variant extends BaseEntity {

    @Column(name = "attribute", nullable = false)
    private String attribute;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private String imageUrl;

    private boolean isDeleted;

    @OneToMany(mappedBy = "variant", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<SizeEntity> sizes = new ArrayList<>();
}