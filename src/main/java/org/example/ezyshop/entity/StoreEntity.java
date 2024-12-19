package org.example.ezyshop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;
import org.example.ezyshop.base.BaseEntity;

@Entity
@Table(name = "stores")
@Data
@Accessors(chain = true)
public class StoreEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column()
    private String description;

    @Column()
    private boolean isDeleted;

    private boolean isApproved;
}
