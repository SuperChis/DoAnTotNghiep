package org.example.ezyshop.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ezyshop.entity.Role;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserDTO {
    private Long id;

    private String username;

    private String email;

    private String sex;

    private String phoneNumber;

    private String avatarUrl;

    private Date birthDay;

    private boolean isDeleted;

    private Set<Role> roles;


    public UserDTO(Long id, String username, String email, String userType, String userLevel,
                   boolean isDeleted, String sex) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.isDeleted = isDeleted;
        this.sex= sex;
    }
}
