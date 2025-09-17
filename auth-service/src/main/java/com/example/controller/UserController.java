package com.example.controller;

import com.example.client.UserClient;
import com.example.domain.entity.AuthUser;
import com.example.domain.entity.Role;
import com.example.domain.request.ChangePasswordRequest;
import com.example.domain.request.UserUpdateDTO;
import com.example.domain.response.ResUserDTO;
import com.example.service.AuthUserService;
import com.example.service.RoleService;
import com.example.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final AuthUserService authUserService;
    private final PasswordEncoder passwordEncoder;
    private final UserClient userClient;
    private final RoleService roleService;

    public UserController(AuthUserService authUserService,
                          PasswordEncoder passwordEncoder,
                          UserClient userClient,
                          RoleService roleService) {
        this.authUserService = authUserService;
        this.passwordEncoder = passwordEncoder;
        this.userClient = userClient;
        this.roleService = roleService;
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

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'USER_ADMIN_UPDATE')")
    public ResponseEntity<ResUserDTO> updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody UserUpdateDTO dto) throws IdInvalidException {
        AuthUser user = authUserService.findById(id).orElseThrow(
                () -> new IdInvalidException("Không tìm thấy user trong hệ thống!")
        );

        if (!user.getEmail().equals(dto.getEmail()) && authUserService.isEmailExist(dto.getEmail())) {
            throw new IdInvalidException("Email đã tồn tại!");
        }

        if (userClient.isPhoneExist(user.getEmail(), dto.getPhone())) {
            throw new IdInvalidException("Phone " + dto.getPhone() + " đã tồn tại.");
        }

        Role role = roleService.findById(dto.getRoleId()).orElse(null);
        if(role == null){
            throw new IdInvalidException("Role không tồn tại!");
        }

        return ResponseEntity.ok(authUserService.updateUser(user, role, dto));
    }

}
