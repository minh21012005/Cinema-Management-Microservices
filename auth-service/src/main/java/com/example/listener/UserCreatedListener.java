package com.example.listener;

import com.example.domain.entity.UserDTO;
import com.example.entity.AuthRole;
import com.example.entity.AuthUser;
import com.example.service.AuthRoleService;
import com.example.service.AuthUserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedListener {
    private final AuthRoleService authRoleService;
    private final AuthUserService authUserService;

    public UserCreatedListener(AuthRoleService authRoleService, AuthUserService authUserService) {
        this.authRoleService = authRoleService;
        this.authUserService = authUserService;
    }

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void handleUserCreated(UserDTO userDTO) {
        AuthUser authUser = new AuthUser();
        AuthRole authRole = this.authRoleService.findById(userDTO.getRoleId());

        authUser.setEmail(userDTO.getEmail());
        authUser.setPassword(userDTO.getPassword());
        authUser.setRole(authRole);

        this.authUserService.saveUser(authUser);
    }

}
