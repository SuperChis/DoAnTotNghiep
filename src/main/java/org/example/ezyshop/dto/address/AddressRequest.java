package org.example.ezyshop.dto.address;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class AddressRequest {
    private Long id;
    private String nameCustomer;
    private String phoneNumber;
    private String specificAddress;
    private String ward; //xã/phường
    private String district; //quận/huyện
    private String province; //tỉnh/TP
    private Boolean defaultAddress;
}
