package com.example.controller;

import com.example.domain.entity.User;
import com.example.domain.request.CreateUserRequest;
import com.example.domain.request.UserUpdateDTO;
import com.example.domain.response.ResUserDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.service.UserService;
import com.example.util.SecurityUtil;
import com.example.util.annotation.ApiMessage;
import com.example.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService service) {
        this.userService = service;
    }

    @GetMapping()
    @ApiMessage("fetch all users")
    @PreAuthorize("hasPermission(null, 'USER_VIEW_ALL')")
    public ResponseEntity<ResultPaginationDTO> getAll(
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "roleId", required = false) Long roleId,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(
                this.userService.fetchAllUser(email, roleId, pageable));
    }

    @GetMapping("/{id}")
    @ApiMessage("Fetch User by ID")
    @PreAuthorize("hasPermission(null, 'USER_VIEW')")
    public ResponseEntity<User> getById(@PathVariable("id") Long id) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.userService.findById(id).orElseThrow(
                        () -> new IdInvalidException("User ID is invalid!")));
    }

    @PostMapping
    @ApiMessage("Create new user")
    @PreAuthorize("hasPermission(null, 'USER_CREATE')")
    public ResponseEntity<ResUserDTO> create(@Valid @RequestBody CreateUserRequest dto) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.createUser(dto));
    }

    @GetMapping("/check-phone")
    public Boolean checkPhone(@RequestParam("phone") String phone) {
        return userService.isPhoneExist(phone);
    }

    @GetMapping("/check-phone-update")
    public Boolean checkPhone(
            @RequestParam("email") String email,
            @RequestParam("newPhone") String newPhone) throws IdInvalidException {
        User user = userService.findByEmail(email).orElseThrow(
                () -> new IdInvalidException("Không tìm thấy user trong hệ thống!")
        );
        if(!user.getPhone().equals(newPhone)){
            return userService.isPhoneExist(newPhone);
        }else {
            return false;
        }
    }

    @PutMapping()
    @ApiMessage("Updated my profile")
    @PreAuthorize("hasPermission(null, 'USER_UPDATE')")
    public ResponseEntity<ResUserDTO> update(
            @Valid @RequestBody UserUpdateDTO dto,
            Authentication authentication
    ) throws IdInvalidException {
        Long id = Long.valueOf(authentication.getName());
        User user = userService.findById(id).orElseThrow(
                () -> new IdInvalidException("Không thấy user trong hệ thống!"));
        if (!dto.getPhone().equals(user.getPhone()) && userService.isPhoneExist(dto.getPhone())) {
            throw new IdInvalidException("Phone " + dto.getPhone() + " đã tồn tại!");
        }
        return ResponseEntity.ok(this.userService.updateUser(user, dto));
    }

    @GetMapping("/fetch-cinema")
    @PreAuthorize("hasPermission(null, 'CINEMA_VIEW')")
    public Long getCinemaIdByUser(Authentication authentication) throws IdInvalidException {
        String email = SecurityUtil.extractPrincipal(authentication);
        if(email == null) {
            throw new IdInvalidException("Email không hợp lệ!");
        }
        User user = userService.findByEmail(email).orElseThrow(
                () -> new IdInvalidException("Không tìm thấy user trong hệ thống!")
        );
        if (user.getCinemaId() == null) {
            throw new IdInvalidException("User không thuộc rạp nào trong hệ thống!");
        }
        return user.getCinemaId();
    }
}
