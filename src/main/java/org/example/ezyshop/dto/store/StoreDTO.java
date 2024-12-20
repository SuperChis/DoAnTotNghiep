package org.example.ezyshop.dto.store;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class StoreDTO {
    private String id;
    private String userName;
    private String userEmail;
    private String name;
    private String address;
    private String description;
    private boolean isApproved;
}
