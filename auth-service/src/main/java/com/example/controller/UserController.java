package com.example.controller;

import com.example.domain.entity.AuthUser;
import com.example.domain.request.ChangePasswordRequest;
import com.example.domain.response.RestResponse;
import com.example.service.AuthUserService;
import com.example.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final AuthUserService authUserService;
    private final PasswordEncoder passwordEncoder;

    public UserController(AuthUserService authUserService, PasswordEncoder passwordEncoder) {
        this.authUserService = authUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasPermission(null, 'USER_CHANGE_PASSWORD')")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Principal principal) throws IdInvalidException {
        AuthUser user = authUserService.findById(Long.valueOf(principal.getName()))
                .orElseThrow(() -> new IdInvalidException("User không tồn tại"));

        // kiểm tra oldPassword có khớp không
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IdInvalidException("Mật khẩu cũ không chính xác");
        }

        // set password mới
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        authUserService.save(user);

        return ResponseEntity.ok("Đổi mật khẩu thành công");
    }
}
