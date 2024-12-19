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

    private String nameCustomer;

    private String phoneNumber;

    @Column(nullable = false) // Địa chỉ cụ thể (số nhà, tên đường...)
    private String specificAddress;

    @Column(nullable = false) // Xã/phường
    private String ward;

    @Column(nullable = false) // Quận/huyện
    private String district;

    @Column(nullable = false) // Tỉnh/thành phố
    private String province;

    @Column()
    private Boolean defaultAddress;

    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
