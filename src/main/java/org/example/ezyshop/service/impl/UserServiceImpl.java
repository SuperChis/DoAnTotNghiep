package org.example.ezyshop.service.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.example.ezyshop.base.BaseResponse;
import org.example.ezyshop.config.jwt.JwtUtils;
import org.example.ezyshop.config.service.UserDetailsImpl;
import org.example.ezyshop.dto.JwtResponse;
import org.example.ezyshop.dto.auth.*;
import org.example.ezyshop.dto.pagination.PageDto;
import org.example.ezyshop.dto.user.UserDTO;
import org.example.ezyshop.dto.user.UserResponse;
import org.example.ezyshop.entity.RefreshToken;
import org.example.ezyshop.entity.Role;
import org.example.ezyshop.entity.User;
import org.example.ezyshop.enums.ERole;
import org.example.ezyshop.exception.AuthenticationFailException;
import org.example.ezyshop.exception.NotFoundException;
import org.example.ezyshop.exception.RequetFailException;
import org.example.ezyshop.mailConfig.EmailService;
import org.example.ezyshop.mapper.UserMapper;
import org.example.ezyshop.repository.RoleRepository;
import org.example.ezyshop.repository.UserRepository;
import org.example.ezyshop.service.RefreshTokenService;
import org.example.ezyshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Value("${EzyShop.app.jwtForgotExpirationMs}")
    private Long forgotJWT;

    @Value("${EzyShop.app.jwtSecret}")
    private String jwtSecret;

    @Value("${EzyShop.app.domain.client}")
    private String domainApp;

    @Override
    public SignUpResponse signUp(SignUpRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new SignUpResponse(false, 400, getErrMsg(bindingResult));
        }
        if (repository.findByEmailAndIsDeletedFalse(request.getEmail()).isPresent()) {
            return new SignUpResponse(false, 400, "Account is exists");
        }

        User user = new User()
                .setUsername(request.getUsername())
                .setEmail(request.getEmail())
                .setPassword(encoder.encode(request.getPassword()))
                .setSex(request.getSex())
                .setDeleted(false);
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new AuthenticationFailException(false, 401, "Error: Role is not found"));
        roles.add(userRole);
        user.setRoles(roles);
        repository.save(user);

        //Gửi email thông báo
//        String subject = "Registration Successful";
//        String text = "Dear " + user.getUsername() + ",\n\nYour registration is successful.\n\nBest regards,\nYour Company";
//        emailService.sendEmail(user.getEmail(), subject, text);
        executorService.submit(() -> {
            try {
                String subject = "Welcome to Our EzyShop!";
                String text = "Dear " + user.getUsername() + ",\n\n" +
                        "Thank you for registering an account with us. We’re excited to have you as part of our community!\n\n" +
                        "With your new account, you can now:\n" +
                        "- Browse and shop from a wide range of products.\n" +
                        "- Enjoy exclusive deals and discounts.\n" +
                        "- Track your orders and manage your preferences easily.\n\n" +
                        "If you have any questions or need assistance, our customer support team is here to help. You can reach us at supportEzyShop@yopmail.com.\n\n" +
                        "Start exploring now and make the most out of your shopping experience!\n\n" +
                        "Best regards,\n" +
                        "The EzyShop Team";
                emailService.sendEmail(user.getEmail(), subject, text);
            } catch (Exception e) {
                System.err.println("Error occurred while sending email: " + e.getMessage());
            }
        });

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(authentication);

        return new SignUpResponse(true, 200)
                .setToken(jwt)
                .setRefreshToken(refreshToken.getToken())
                .setRoles(roles);
    }

    @Override
    public JwtResponse signIn(SignInRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RequetFailException(false, 400, getErrMsg(bindingResult));
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("Authentication: " + authentication.getPrincipal());
        System.out.println(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return new JwtResponse(jwt, refreshToken.getToken(), roles, true, 200);
    }

    @Override
    public Optional<User> findByUserName(String username) {
        return repository.findByUsernameAndIsDeletedFalse(username);
    }

    @Override
    public UserResponse getListUser(Pageable pageable) {
        Page<User> userPage = repository.findAll(pageable);
        List<UserDTO> dtoList = userPage.getContent()
                .stream()
                .map(UserMapper.Mapper::toDTO)
                .collect(Collectors.toList());
        UserResponse response = new UserResponse(true, 200)
                .setUserDTOList(dtoList)
                .setPageDto(PageDto.populatePageDto(userPage));
        return response;
    }

    @Override
    public BaseResponse resetPasswordByAdmin(ResetPasswordRequest request) {
        Optional<User> userOptional = repository.findByIdAndIsDeletedFalse(request.getUserId());
        if (userOptional.isEmpty()) {
            throw new NotFoundException(false, 404, "User not exists");
        }
        if (request == null || request.getPassword().isEmpty()) {
            throw new RequetFailException(false, 400, "password is not empty");
        }
        User user = userOptional.get();
        user.setPassword(encoder.encode(request.getPassword()));
        repository.save(user);
        return new BaseResponse(true, 200, "change password succesfully");
    }

    @Override
    @Transactional
    public BaseResponse resetPasswordByUser(ResetPasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        user.setPassword(encoder.encode(request.getPassword()));
        repository.save(user);
        return new BaseResponse(true, 200, "change password succesfully");
    }

    @Override
    public BaseResponse deactiveUser(Long userId) {
        Optional<User> userOptional = repository.findById(userId);
        if (userOptional.isEmpty() || userOptional == null) {
            throw new NotFoundException(false, 404, "user not exists");
        }
        User user = userOptional.get();
        if (user.isDeleted()) {
            user.setDeleted(false);
        } else {
            user.setDeleted(true);
        }
        repository.save(user);
        return new BaseResponse(true, 200, "User id: " + userId + " is active: " + user.isDeleted());
    }

    @Override
    public BaseResponse sendPasswordResetToken(ForgetPasswordRequest request) {
        User existUser = userRepository.findByEmailAndIsDeletedFalse(request.getEmail())
                .orElseThrow(() -> new NotFoundException(false, 404, "User not exist"));

        String forgotPasswordJwt = Jwts.builder()
                .setSubject(existUser.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + forgotJWT))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)), SignatureAlgorithm.HS256)
                .compact();

        executorService.submit(() -> {
           try {
               StringBuilder resetUrlBuilder = new StringBuilder("http://");
               resetUrlBuilder.append(domainApp);
               resetUrlBuilder.append("/reset-password?token=");
               resetUrlBuilder.append(forgotPasswordJwt);
               String resetUrl = resetUrlBuilder.toString();

               String subject = "Reset Your Password";
               String text = "Dear " + existUser.getUsername() + ",\n\n" +
                       "Click the link below to reset your password:\n" +
                       resetUrl + "\n\n" +
                       "If you didn’t request a password reset, please ignore this email.";

               emailService.sendEmail(existUser.getEmail(), subject, text);
           } catch (Exception e) {
               System.out.println(e.getMessage());
           }
        });
        return new BaseResponse(true, 200, "A password reset email has been sent to your email address. Please check your email!!!");
    }

    @Override
    public BaseResponse resetPasswordForgot(String token, ForgetPasswordRequest request) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();
            User user = userRepository.findByEmailAndIsDeletedFalse(email)
                    .orElseThrow(() -> new NotFoundException(false, 404, "User does not exist"));

            user.setPassword(encoder.encode(request.getPassword()));
            userRepository.save(user);

        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token has expired");
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid token");
        }
        return new BaseResponse(true, 200, "Your password has been successfully reset");
    }

    @Override
    public UserResponse getUerProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        UserDTO dto = UserMapper.Mapper.toDTO(user);
        return new UserResponse(true, 200)
                .setUserDTO(dto);
    }


//    @Override
//    public JwtResponse signInWithGooggle(GoogleSignInRequest request) {
//        // Xác thực ID Token từ Google
//        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
//                .setAudience(Collections.singletonList("YOUR_GOOGLE_CLIENT_ID"))
//                .build();
//
//        try {
//            GoogleIdToken idToken = verifier.verify(request.getIdToken());
//            if (idToken != null) {
//                GoogleIdToken.Payload payload = idToken.getPayload();
//
//                String email = payload.getEmail();
//                String name = (String) payload.get("name");
//
//                // Kiểm tra người dùng trong hệ thống
//                User user = userRepository.findByEmail(email)
//                        .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
//
//                // Tạo Authentication object cho người dùng
//                UserDetailsImpl userDetails = UserDetailsImpl.build(user);
//                UsernamePasswordAuthenticationToken authenticationToken =
//                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//
//                // Sinh JWT và Refresh Token
//                String jwt = jwtUtils.generateJwtToken(authenticationToken);
//                RefreshToken refreshToken = refreshTokenService.createRefreshToken(authenticationToken);
//
//                List<String> roles = userDetails.getAuthorities().stream()
//                        .map(GrantedAuthority::getAuthority)
//                        .collect(Collectors.toList());
//
//                return new JwtResponse(jwt, refreshToken.getToken(), roles);
//            } else {
//                throw new AuthenticationFailException(false, 401, "Invalid Google ID Token");
//            }
//        } catch (Exception e) {
//            throw new AuthenticationFailException(false, 401, "Google authentication failed: " + e.getMessage());
//        }
//    }

//    @Override
//    public UserResponse search(String search) {
//        List<UserDTO> dto = repository.search(search);
//        return new UserResponse(true, 200).setUserDTOList(dto);
//    }


    private String getErrMsg(BindingResult bindingResult) {
        Map<String, String> errorMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        StringBuilder errorMsg = new StringBuilder();
        for (String key : errorMap.keySet()) {
            errorMsg.append("Field Error: ").append(key).append("; Reason: ").append(errorMap.get(key)).append("\n");
        }
        return errorMsg.toString();
    }
}
