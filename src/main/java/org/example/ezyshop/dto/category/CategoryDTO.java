package org.example.ezyshop.dto.category;

import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;

    private String name;

    private String description;

    private Long quantity;

    public CategoryDTO() {
    }

    public CategoryDTO(Long id, String name, String description, Long quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
    }
}
