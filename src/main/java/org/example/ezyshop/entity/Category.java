package org.example.ezyshop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.example.ezyshop.base.BaseEntity;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Category extends BaseEntity {
    @Column()
    private String name;

    @Column()
    private String description;
}
