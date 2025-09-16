package com.example.listener;

import com.example.domain.entity.AuthUser;
import com.example.domain.entity.Role;
import com.example.domain.entity.UserAuthDTO;
import com.example.service.AuthUserService;
import com.example.service.RoleService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedByAdminListener {

    private final AuthUserService authUserService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserCreatedByAdminListener(AuthUserService authUserService, PasswordEncoder passwordEncoder,
                                      RoleService roleService) {
        this.authUserService = authUserService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @RabbitListener(queues = "${app.rabbitmq.listen-queue}")
    public void handleUserCreated(UserAuthDTO userAuthDTO) {

        String hashedPassword = passwordEncoder.encode(userAuthDTO.getPassword());
        Role role = roleService.findById(userAuthDTO.getRoleId()).orElse(
                roleService.findByCode("CUSTOMER").orElse(null));
        // Tạo User profile
        AuthUser user = new AuthUser();
        user.setEmail(userAuthDTO.getEmail());
        user.setPassword(hashedPassword);
        user.setRole(role);
        authUserService.save(user);
    }
}

