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
    private String nameCustomer;
    private String phoneNumber;
    private String specificAddress;
    private String ward; //xã/phường
    private String district; //quận/huyện
    private String province; //tỉnh/TP
    private String fullAddress;
    private Boolean defaultAddress;
    private Date created;
    private Date lastUpdate;
}
