package org.example.ezyshop.dto.store;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class CreateStoreRequest {
    private String name;
    private String address;
    private String description;
}
