package org.example.ezyshop.controller;


import jakarta.validation.Valid;
import org.example.ezyshop.dto.JwtResponse;
import org.example.ezyshop.dto.auth.SignInRequest;
import org.example.ezyshop.dto.auth.SignUpRequest;
import org.example.ezyshop.dto.auth.SignUpResponse;
import org.example.ezyshop.dto.refreshToken.RefreshTokenRequest;
import org.example.ezyshop.dto.refreshToken.RefreshTokenResponse;
import org.example.ezyshop.service.RefreshTokenService;
import org.example.ezyshop.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*; 

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    @Autowired
    private UserServiceImpl service;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/sign-in")
    public JwtResponse singIn(@RequestBody @Valid SignInRequest request, BindingResult bindingResult) {
        return service.signIn(request, bindingResult);
    }

    @PostMapping("/sign-up")
    public SignUpResponse singUp(@RequestBody @Valid SignUpRequest request, BindingResult bindingResult) {
        return service.signUp(request, bindingResult);
    }

    @PostMapping("/refresh-token")
    public RefreshTokenResponse refreshTokenByUser(@Valid @RequestBody RefreshTokenRequest request) {
        return refreshTokenService.refreshTokenForUser(request);
    }
}
