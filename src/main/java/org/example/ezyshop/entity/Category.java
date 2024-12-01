package org.example.ezyshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.example.ezyshop.base.BaseEntity;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Category extends BaseEntity {
    @Column()
    private String name;

    @Column()
    private String description;

    @OneToMany(mappedBy = "category", cascade =  CascadeType.ALL)
    List<Product> products;
}
