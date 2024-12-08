package org.example.ezyshop.controller;


import jakarta.validation.Valid;
import org.example.ezyshop.base.BaseResponse;
import org.example.ezyshop.dto.JwtResponse;
import org.example.ezyshop.dto.auth.*;
import org.example.ezyshop.dto.refreshToken.RefreshTokenRequest;
import org.example.ezyshop.dto.refreshToken.RefreshTokenResponse;
import org.example.ezyshop.service.RefreshTokenService;
import org.example.ezyshop.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public SignUpResponse singUp(@RequestBody SignUpRequest request, BindingResult bindingResult) {
        return service.signUp(request, bindingResult);
    }

    @PostMapping("/refresh-token")
    public RefreshTokenResponse refreshTokenByUser(@RequestBody RefreshTokenRequest request) {
        return refreshTokenService.refreshTokenForUser(request);
    }

    @PutMapping("/user/reset-pass-word")
    public BaseResponse resetPasswordByUser(@RequestBody ResetPasswordRequest request){
        return service.resetPasswordByUser(request);
    }

    @PostMapping("/forgot-password")
    public BaseResponse forgotPassword(@RequestBody ForgetPasswordRequest request) {
        return service.sendPasswordResetToken(request);
    }

    @PostMapping("/reset-password")
    public BaseResponse resetPassword(@RequestParam("token") String token,
                                           @RequestBody ForgetPasswordRequest request) {
        return service.resetPasswordForgot(token, request);
    }
}
