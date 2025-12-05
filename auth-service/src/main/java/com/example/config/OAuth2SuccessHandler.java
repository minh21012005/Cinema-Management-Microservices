package com.example.config;

import com.example.domain.entity.RoleDTO;
import com.example.domain.response.ResLoginDTO;
import com.example.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    public OAuth2SuccessHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String role = oAuth2User.getAttribute("role");

        // Tạo JWT
        ResLoginDTO resLoginDTO = new ResLoginDTO();
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName(role);
        resLoginDTO.setUser(new ResLoginDTO.UserLogin(0L, email, roleDTO));

        String jwtToken = jwtUtil.createAccessToken(email, resLoginDTO);
        String refreshToken = jwtUtil.createRefreshToken(resLoginDTO);

        // Gửi token về FE (có thể header hoặc redirect)
        response.sendRedirect("http://localhost:5173?token=" + jwtToken);
    }
}

