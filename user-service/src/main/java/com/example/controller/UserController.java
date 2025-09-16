package com.example.controller;

import com.example.domain.User;
import com.example.domain.request.CreateUserRequest;
import com.example.domain.response.ResUserDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.service.UserService;
import com.example.util.annotation.ApiMessage;
import com.example.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
            @RequestParam(name = "role", required = false) String role,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(
                this.userService.fetchAllUser(email, role, pageable));
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
}
