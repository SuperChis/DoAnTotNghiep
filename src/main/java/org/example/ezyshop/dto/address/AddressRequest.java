package org.example.ezyshop.dto.address;

import lombok.Data;

@Data
public class AddressRequest {
    private Long id;
    private String street;
    private String city;
    private String state;
    private Boolean defaultAddress;
}
