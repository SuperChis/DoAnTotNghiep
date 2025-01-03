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
import org.example.ezyshop.dto.user.UserRequest;
import org.example.ezyshop.dto.user.UserResponse;
import org.example.ezyshop.entity.Cart;
import org.example.ezyshop.entity.RefreshToken;
import org.example.ezyshop.entity.Role;
import org.example.ezyshop.entity.User;
import org.example.ezyshop.enums.ERole;
import org.example.ezyshop.exception.AuthenticationFailException;
import org.example.ezyshop.exception.NotFoundException;
import org.example.ezyshop.exception.RequetFailException;
import org.example.ezyshop.mailConfig.EmailService;
import org.example.ezyshop.mapper.UserMapper;
import org.example.ezyshop.repository.CartRepository;
import org.example.ezyshop.repository.RefreshTokenRepository;
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
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Value("${EzyShop.app.jwtForgotExpirationMs}")
    private Long forgotJWT;

    @Value("${EzyShop.app.jwtSecret}")
    private String jwtSecret;

    @Value("${EzyShop.app.domain.client}")
    private String domainApp;

    @Value("${EzyShop.app.domain.server}")
    private String domainServer;

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
        user.setCreated(new Date());
        user.setLastUpdate(new Date());

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new AuthenticationFailException(false, 401, "Error: Role is not found"));
        roles.add(userRole);
        user.setRoles(roles);
        user.setStore(false);
        repository.save(user);

        //Create Cart for user
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(0.0);
        cart.setIsDeleted(false);
        cartRepository.save(cart);

        executorService.submit(() -> {
            try {
                executorService.submit(() -> {
                    try {
                        String subject = "Chào mừng đến với TokooShop của chúng tôi!";
                        String text = "Kính gửi " + user.getUsername() + ",\n\n" +
                                "Cảm ơn bạn đã đăng ký tài khoản với chúng tôi. Chúng tôi rất vui khi có bạn trở thành một phần của cộng đồng TokooShop!\n\n" +
                                "Với tài khoản mới, bạn có thể:\n" +
                                "- Duyệt và mua sắm từ một loạt sản phẩm phong phú.\n" +
                                "- Tận hưởng các ưu đãi và giảm giá độc quyền.\n" +
                                "- Theo dõi đơn hàng và dễ dàng quản lý các tùy chọn cá nhân.\n\n" +
                                "Nếu bạn có bất kỳ câu hỏi nào hoặc cần hỗ trợ, đội ngũ chăm sóc khách hàng của chúng tôi luôn sẵn sàng giúp đỡ. Bạn có thể liên hệ với chúng tôi qua email tokoosystem@gmail.com.\n\n" +
                                "Hãy bắt đầu khám phá ngay và tận hưởng trải nghiệm mua sắm tốt nhất!\n\n" +
                                "Trân trọng,\n" +
                                "Đội ngũ TokooShop";
                        emailService.sendEmail(user.getEmail(), subject, text);
                    } catch (Exception e) {
                        System.err.println("Đã xảy ra lỗi khi gửi email: " + e.getMessage());
                    }
                });
            } catch (Exception e) {
                System.err.println("Error occurred while sending email: " + e.getMessage());
            }
        });

//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String jwt = jwtUtils.generateJwtToken(authentication);
//        RefreshToken refreshToken = refreshTokenService.createRefreshToken(authentication);

        return new SignUpResponse(true, 200)
//                .setToken(jwt)
//                .setRefreshToken(refreshToken.getToken())
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
    @Transactional
    public BaseResponse logOut() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserId(user.getId());
        if (refreshToken.isPresent()) {
            refreshTokenService.deleteByUser(user);
        }
        SecurityContextHolder.clearContext();
        return new BaseResponse(true, 200, "Logout successfully");
    }

    @Override
    public Optional<User> findByEmail(String username) {
        return repository.findByEmailAndIsDeletedFalse(username);
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
    public BaseResponse resetPasswordByAdmin(ChangePasswordRequest request) {
        Optional<User> userOptional = repository.findByIdAndIsDeletedFalse(request.getUserId());
        if (userOptional.isEmpty()) {
            throw new NotFoundException(false, 404, "User not exists");
        }
        if (request == null || request.getNewPassword().isEmpty()) {
            throw new RequetFailException(false, 400, "password is not empty");
        }
        User user = userOptional.get();
        user.setPassword(encoder.encode(request.getNewPassword()));
        user.setLastUpdate(new Date());
        repository.save(user);
        return new BaseResponse(true, 200, "change password succesfully");
    }

    @Override
    @Transactional
    public BaseResponse changePasswordByUser(ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        if (!encoder.matches(request.getOldPassword(), user.getPassword())) {
            return new BaseResponse(false, 400, "Current password is incorrect");
        }

        user.setPassword(encoder.encode(request.getNewPassword()));
        user.setLastUpdate(new Date());
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
               resetUrlBuilder.append("/auth/reset-password?token=");
               resetUrlBuilder.append(forgotPasswordJwt);
               String resetUrl = resetUrlBuilder.toString();

               String subject = "Đặt lại mật khẩu của bạn";
               String text = "Kính gửi " + existUser.getUsername() + ",\n\n" +
                       "Nhấp vào liên kết bên dưới để đặt lại mật khẩu của bạn:\n" +
                       resetUrl + "\n\n" +
                       "Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.";

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
            user.setLastUpdate(new Date());
            userRepository.save(user);

        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token has expired");
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid token");
        }
        return new BaseResponse(true, 200, "Your password has been successfully reset");
    }

    @Override
    public UserResponse getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        UserDTO dto = UserMapper.Mapper.toDTO(user);
        return new UserResponse(true, 200)
                .setUserDTO(dto);
    }

    @Override
    @Transactional
    public UserResponse updateProfile(UserRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        UserMapper.Mapper.updateUserFromRequest(request, user);
        user.setLastUpdate(new Date());
        repository.save(user);
        return new UserResponse(true, 200)
                .setUserDTO(UserMapper.Mapper.toDTO(user));
    }

    @Override
    public UserResponse updateAvatar(String fileUrl) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();
        user.setAvatarUrl(fileUrl);
        repository.save(user);
        UserDTO dto = UserMapper.Mapper.toDTO(user);
        return new UserResponse(true, 200).setUserDTO(dto);
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
