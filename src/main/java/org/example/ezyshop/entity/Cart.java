package org.example.ezyshop.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;
import org.example.ezyshop.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Cart extends BaseEntity {

    private Double totalPrice;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean isDeleted;
}
