package org.example.ezyshop.service;

import org.example.ezyshop.base.BaseResponse;
import org.example.ezyshop.dto.JwtResponse;
import org.example.ezyshop.dto.auth.*;
import org.example.ezyshop.dto.user.UserResponse;
import org.example.ezyshop.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Service
public interface UserService {

    SignUpResponse signUp(SignUpRequest request, BindingResult bindingResult);

    JwtResponse signIn(SignInRequest request, BindingResult bindingResult);

    Optional<User> findByUserName(String username);

    UserResponse getListUser(Pageable pageable);

    BaseResponse resetPasswordByAdmin(ResetPasswordRequest request);

    BaseResponse resetPasswordByUser(ResetPasswordRequest request);

    BaseResponse deactiveUser(Long userId);

//    UserResponse search(String search);

//    JwtResponse signInWithGooggle(GoogleSignInRequest request);

    BaseResponse sendPasswordResetToken(ForgetPasswordRequest request);

    BaseResponse resetPasswordForgot(String token, ForgetPasswordRequest request);

    UserResponse getUerProfile();
}
