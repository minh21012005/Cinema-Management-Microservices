package com.example.handler;

import com.example.domain.entity.AuthUser;
import com.example.domain.entity.Permission;
import com.example.domain.entity.RoleDTO;
import com.example.domain.response.ResLoginDTO;
import com.example.service.AuthUserService;
import com.example.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private ObjectMapper objectMapper;

    public OAuth2SuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        // 1) TÌM USER TRONG DB
        AuthUser userDB = authUserService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email không tồn tại trong hệ thống"));

        if (!userDB.isEnabled()) {
            throw new IllegalArgumentException("Tài khoản đang bị khóa, không thể đăng nhập!");
        }

        // 2) ROLE + PERMISSIONS
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(userDB.getRole().getId());
        roleDTO.setName(userDB.getRole().getCode());

        List<String> permissions = userDB.getRole().getPermissions()
                .stream()
                .filter(Permission::isActive)
                .map(Permission::getCode)
                .toList();

        // Lưu vào Redis
        String redisKey = "user:permissions:" + userDB.getId();
        redisTemplate.delete(redisKey);

        Map<String, String> permissionMap = new HashMap<>();
        permissions.forEach(perm -> permissionMap.put(perm, "1"));

        redisTemplate.opsForHash().putAll(redisKey, permissionMap);
        redisTemplate.expire(redisKey, 1, TimeUnit.HOURS);

        // 3) RES DTO
        ResLoginDTO res = new ResLoginDTO();
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                userDB.getId(),
                userDB.getEmail(),
                roleDTO
        );
        res.setUser(userLogin);

        // 4) ACCESS TOKEN
        String accessToken = jwtUtil.createAccessToken(userDB.getEmail(), res);
        res.setAccessToken(accessToken);

        // 5) REFRESH TOKEN
        String refreshToken = jwtUtil.createRefreshToken(res);
        String hashedRefreshToken = passwordEncoder.encode(refreshToken);

        authUserService.updateRefreshToken(hashedRefreshToken, userDB.getEmail());

        // ENCODE USER JSON
        String userJson = URLEncoder.encode(
                objectMapper.writeValueAsString(userLogin),
                StandardCharsets.UTF_8
        );

        ResponseCookie cookie = ResponseCookie
                .from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(30 * 24 * 60 * 60)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        String redirectUrl = "http://localhost:5173/social-login";

        response.sendRedirect(redirectUrl);
    }
}

