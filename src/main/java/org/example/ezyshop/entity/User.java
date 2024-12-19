package org.example.ezyshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.example.ezyshop.base.BaseEntity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "users")
public class User extends BaseEntity {

    private String username;

    private String email;

    private String password;

    private String sex;

    private Date birthDay;

    private String phoneNumber;

    private String avatarUrl;

    private boolean isStore;

    private boolean isDeleted;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", referencedColumnName = "id")
    private StoreEntity store;

    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }

}
