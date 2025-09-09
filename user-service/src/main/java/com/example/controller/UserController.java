package com.example.controller;

import com.example.domain.entity.UserDTO;
import com.example.domain.response.ResCreateUserDTO;
import com.example.entity.User;
import com.example.service.UserService;
import com.example.util.annotation.ApiMessage;
import com.example.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RabbitTemplate rabbitTemplate;

    public UserController(UserService userService, PasswordEncoder passwordEncoder,
                          RabbitTemplate rabbitTemplate) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/auth/register")
    @ApiMessage("Register a new user")
    public ResponseEntity<ResCreateUserDTO> register(@Valid @RequestBody User postManUser) throws IdInvalidException {
        boolean isEmailExist = this.userService.isEmailExist(postManUser.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException(
                    "Email " + postManUser.getEmail() + "đã tồn tại, vui lòng sử dụng email khác.");
        }

        boolean isPhoneExist = this.userService.isPhoneExist(postManUser.getPhone());
        if (isPhoneExist) {
            throw new IdInvalidException(
                    "Số điện thoại " + postManUser.getPhone() + " đã tồn tại, vui lòng sử dụng số điện thoại khác.");
        }

        String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
        postManUser.setPassword(hashPassword);
        User savedUser = this.userService.handleCreateUser(postManUser);

        // Publish event sang auth-service
        this.rabbitTemplate.convertAndSend(
                "user.exchange", "user.created",
                new UserDTO(savedUser.getId(), savedUser.getEmail(), savedUser.getPassword(), savedUser.getRole().getId())
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(savedUser));
    }
}
