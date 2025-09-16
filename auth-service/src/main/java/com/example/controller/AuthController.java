package com.example.controller;

import com.example.domain.entity.RoleDTO;
import com.example.domain.entity.UserProfileDTO;
import com.example.domain.request.CreateUserRequest;
import com.example.domain.request.ReqLoginDTO;
import com.example.domain.response.ResLoginDTO;
import com.example.domain.response.ResUserDTO;
import com.example.domain.entity.AuthUser;
import com.example.domain.entity.Permission;
import com.example.domain.entity.Role;
import com.example.service.AuthUserService;
import com.example.service.RoleService;
import com.example.client.UserClient;
import com.example.util.JwtUtil;
import com.example.util.annotation.ApiMessage;
import com.example.util.error.IdInvalidException;
import com.example.util.error.UnauthorizedException;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Value("${app.rabbitmq.send-routing-key}")
    private String sendRoutingKey;

    @Value("${app.rabbitmq.exchange}")
    private String exchangeName;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserService authUserService;
    private final RoleService roleService;
    private final RabbitTemplate rabbitTemplate;
    private final UserClient userClient;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

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

    @PostMapping("/register")
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

        Role role = this.roleService.findByCode("CUSTOMER").orElse(null);

        AuthUser authUser = new AuthUser();
        authUser.setEmail(userRequest.getEmail());
        authUser.setPassword(hashedPassword);
        authUser.setRole(role);

        AuthUser savedUser = this.authUserService.save(authUser);

        UserProfileDTO profileEvent = new UserProfileDTO(
                userRequest.getName(),
                userRequest.getPhone(),
                userRequest.getDateOfBirth(),
                userRequest.getGender(),
                userRequest.getEmail(),
                userRequest.getAddress(),
                role.getCode() != null ? role.getCode() : "CUSTOMER"
        );

        // Publish event sang auth-service
        this.rabbitTemplate.convertAndSend(
                exchangeName, sendRoutingKey, profileEvent
        );

        ResUserDTO res = new ResUserDTO();
        res.setId(savedUser.getId());
        res.setName(userRequest.getName());
        res.setEmail(userRequest.getEmail());
        res.setRole(role.getCode());

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping("/login")
    @ApiMessage("Login successfully")
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
        AuthUser currentUserDB = this.authUserService.findByEmail(loginDto.getUsername()).orElse(null);
        if (currentUserDB != null) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(currentUserDB.getRole().getId());
            roleDTO.setName(currentUserDB.getRole().getCode());

            List<String> permissions = currentUserDB.getRole().getPermissions().stream()
                    .map(Permission::getCode)
                    .toList();

            String redisKey = "user:permissions:" + currentUserDB.getId();
            Map<String, String> permissionMap = new HashMap<>();
            permissions.forEach(perm -> permissionMap.put(perm, "1"));
            redisTemplate.opsForHash().putAll(redisKey, permissionMap);
            redisTemplate.expire(redisKey, 1, TimeUnit.HOURS);

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
        String refresh_token = this.jwtUtil.createRefreshToken(res);

        // Hash refresh token bằng PasswordEncoder
        String hashedRefreshToken = passwordEncoder.encode(refresh_token);
        this.authUserService.updateRefreshToken(hashedRefreshToken, loginDto.getUsername());

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

    @GetMapping("/account")
    @ApiMessage("fetch account")
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount(
            @RequestHeader(value = "X-User-Email", required = false) String email) throws UnauthorizedException {

        if (email == null || email.isEmpty()) {
            throw new UnauthorizedException("Token không hợp lệ");
        }

        AuthUser currentUserDB = this.authUserService.findByEmail(email).orElse(null);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();

        if (currentUserDB != null) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(currentUserDB.getRole().getId());
            roleDTO.setName(currentUserDB.getRole().getCode());

            List<String> permissions = currentUserDB.getRole().getPermissions().stream()
                    .map(Permission::getCode)
                    .toList();

            userLogin.setId(currentUserDB.getId());
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setRole(roleDTO);
            userGetAccount.setUser(userLogin);
        }

        return ResponseEntity.ok().body(userGetAccount);
    }

    @PostMapping("/refresh")
    @ApiMessage("Get User by refresh token")
    public ResponseEntity<ResLoginDTO> getRefreshToken(
            @CookieValue(name = "refresh_token", required = false) String refreshTokenRaw)
            throws IdInvalidException, UnauthorizedException {
        if (refreshTokenRaw == null || refreshTokenRaw.isEmpty()) {
            throw new IdInvalidException("Bạn không có refresh token ở cookie");
        }
        // 1. Check valid JWT format + expiration
        Jwt decodedToken = this.jwtUtil.checkValidRefreshToken(refreshTokenRaw);
        String email = decodedToken.getClaim("email");

        // 2. Lấy user trong DB
        AuthUser currentUser = this.authUserService.findByEmail(email)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy user"));

        // 3. So sánh token client gửi với token hash trong DB
        String hashedTokenInDb = currentUser.getRefreshToken();
        if (hashedTokenInDb == null || !passwordEncoder.matches(refreshTokenRaw, hashedTokenInDb)) {
            throw new IdInvalidException("Refresh Token không hợp lệ");
        }

        // issue new token/set refresh token as cookies
        ResLoginDTO res = new ResLoginDTO();
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(currentUser.getRole().getId());
        roleDTO.setName(currentUser.getRole().getCode());

        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                currentUser.getId(),
                currentUser.getEmail(),
                roleDTO);
        res.setUser(userLogin);

        // 4. Issue access token mới
        String access_token = this.jwtUtil.createAccessToken(email, res);
        res.setAccessToken(access_token);

        // 5. Issue refresh token mới + hash lại
        String newRefreshToken = this.jwtUtil.createRefreshToken(res);
        String hashedNewRefreshToken = passwordEncoder.encode(newRefreshToken);
        this.authUserService.updateRefreshToken(hashedNewRefreshToken, email);

        String redisKey = "user:permissions:" + currentUser.getId();

        // Xóa cache cũ
        redisTemplate.delete(redisKey);

        // Thiết lập lại cache mới
        List<String> permissions = currentUser.getRole().getPermissions().stream()
                .map(Permission::getCode)
                .toList();

        Map<String, String> permissionMap = new HashMap<>();
        permissions.forEach(perm -> permissionMap.put(perm, "1"));
        redisTemplate.opsForHash().putAll(redisKey, permissionMap);
        redisTemplate.expire(redisKey, 1, TimeUnit.HOURS);

        // set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);
    }

    @PostMapping("/logout")
    @ApiMessage("Logout User")
    public ResponseEntity<Void> logout(
            @RequestHeader(value = "X-User-Email", required = false) String email) throws UnauthorizedException {

        if (email == null || email.isEmpty()) {
            throw new UnauthorizedException("Token không hợp lệ");
        }

        AuthUser user = this.authUserService.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UnauthorizedException("User không tồn tại trong hệ thống");
        }

        // update refresh token = null
        this.authUserService.updateUserToken(null, email);

        // Xóa cache cũ
        String redisKey = "user:permissions:" + user.getId();
        redisTemplate.delete(redisKey);

        // remove refresh token cookie
        ResponseCookie deleteSpringCookie = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                .body(null);
    }
}
