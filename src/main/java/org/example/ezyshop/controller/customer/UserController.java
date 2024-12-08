package org.example.ezyshop.controller.customer;

import org.example.ezyshop.dto.user.UserResponse;
import org.example.ezyshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/profile")
    public UserResponse getListUserByAdmin() {
        return service.getUerProfile();
    }
}
