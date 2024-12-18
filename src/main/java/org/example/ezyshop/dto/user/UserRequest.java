package org.example.ezyshop.dto.user;

import lombok.Data;

import java.util.Date;

@Data
public class UserRequest {

//    private String email;

    private String username;

    private String phoneNumber;

    private String avatarUrl;

    private Date birthDay;

    private String sex;
}
