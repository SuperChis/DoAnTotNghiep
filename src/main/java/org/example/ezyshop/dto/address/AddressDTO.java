package org.example.ezyshop.dto.address;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AddressDTO {
    private Long id;
    private String street;
    private String city;
    private String state;
    private Boolean defaultAddress;
    private Date created;
    private Date lastUpdate;
}
