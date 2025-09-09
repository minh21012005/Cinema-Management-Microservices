package com.example.controller;

import com.example.domain.entity.RoleDTO;
import com.example.domain.entity.UserProfileDTO;
import com.example.domain.request.CreateUserRequest;
import com.example.domain.request.ReqLoginDTO;
import com.example.domain.response.ResLoginDTO;
import com.example.domain.response.ResUserDTO;
import com.example.entity.AuthUser;
import com.example.entity.Role;
import com.example.service.AuthUserService;
import com.example.service.RoleService;
import com.example.client.UserClient;
import com.example.util.JwtUtil;
import com.example.util.SecurityUtil;
import com.example.util.annotation.ApiMessage;
import com.example.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserService authUserService;
    private final RoleService roleService;
    private final RabbitTemplate rabbitTemplate;
    private final UserClient userClient;

    @Value("${minhnb.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            JwtUtil jwtUtil, PasswordEncoder passwordEncoder,
            AuthUserService authUserService,
            RoleService roleService,
            RabbitTemplate rabbitTemplate,
            UserClient userClient) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authUserService = authUserService;
        this.roleService = roleService;
        this.rabbitTemplate = rabbitTemplate;
        this.userClient = userClient;
    }

    @PostMapping("/auth/register")
    @ApiMessage("Register a new user")
    public ResponseEntity<ResUserDTO> register(@Valid @RequestBody CreateUserRequest userRequest) throws IdInvalidException {
        boolean isEmailExist = this.authUserService.isEmailExist(userRequest.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException(
                    "Email " + userRequest.getEmail() + " đã tồn tại, vui lòng sử dụng email khác.");
        }

        if (userClient.isPhoneExist(userRequest.getPhone())) {
            throw new IdInvalidException("Phone " + userRequest.getPhone() + " đã tồn tại.");
        }

        String hashedPassword = passwordEncoder.encode(userRequest.getPassword());

        Role role = this.roleService.findByName("CUSTOMER");

        AuthUser authUser = new AuthUser();
        authUser.setEmail(userRequest.getEmail());
        authUser.setPassword(hashedPassword);
        authUser.setRole(role);

        AuthUser savedUser = this.authUserService.saveUser(authUser);

        UserProfileDTO profileEvent = new UserProfileDTO(
                savedUser.getId(),
                userRequest.getName(),
                userRequest.getPhone(),
                userRequest.getDateOfBirth(),
                userRequest.getGender(),
                userRequest.getEmail(),
                userRequest.getAddress()
        );

        // Publish event sang auth-service
        this.rabbitTemplate.convertAndSend(
                "user.exchange", "user.created",profileEvent
        );

        ResUserDTO res = new ResUserDTO();
        res.setId(savedUser.getId());
        res.setName(userRequest.getName());
        res.setEmail(userRequest.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDto) {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword());

        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        // set thông tin người dùng đăng nhập vào context (có thể sử dụng sau này)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO res = new ResLoginDTO();
        AuthUser currentUserDB = this.authUserService.handleGetUserByUsername(loginDto.getUsername());
        if (currentUserDB != null) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(currentUserDB.getRole().getId());
            roleDTO.setName(currentUserDB.getRole().getName());

            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    currentUserDB.getId(),
                    currentUserDB.getEmail(),
                    roleDTO);
            res.setUser(userLogin);
        }

        // create access token
        String access_token = this.jwtUtil.createAccessToken(authentication.getName(), res);
        res.setAccessToken(access_token);

        // create refresh token
        String refresh_token = this.jwtUtil.createRefreshToken(loginDto.getUsername(), res);

        // update user
        this.authUserService.updateUserToken(refresh_token, loginDto.getUsername());

        // set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);
    }
}
