package org.example.ezyshop.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ezyshop.entity.Role;

import java.util.Set;

@Data
@NoArgsConstructor
public class UserDTO {
    private Long id;

    private String username;

    private String email;

    private String userType;

    private String userLevel;

    private String sex;

    private boolean isDeleted;

    private Set<Role> roles;


    public UserDTO(Long id, String username, String email, String userType, String userLevel,
                   boolean isDeleted, String sex) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.userType = userType;
        this.userLevel = userLevel;
        this.isDeleted = isDeleted;
        this.sex= sex;
    }
}
