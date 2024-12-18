package org.example.ezyshop.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ezyshop.base.BaseEntity;
import org.example.ezyshop.enums.ERole;

@Entity
@Data
@NoArgsConstructor
@Table(name = "roles")
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private ERole name;

    public Role(ERole name){
        this.name = name;
    }

}
