package org.example.ezyshop.entity;

import jakarta.persistence.*;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private boolean isApproved;
}
