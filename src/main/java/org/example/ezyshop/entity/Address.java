package org.example.ezyshop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.experimental.Accessors;
import org.example.ezyshop.base.BaseEntity;

@Entity
@Data
@Accessors(chain = true)
public class Address extends BaseEntity {

    @Column()
    private String street;

    @Column()
    private String city;

    @Column()
    private String state;

    @Column()
    private Boolean defaultAddress;

    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
