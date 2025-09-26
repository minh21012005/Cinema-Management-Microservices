package com.example.service.impl;

import com.example.domain.entity.AuthUser;
import com.example.domain.entity.Role;
import com.example.domain.request.CreateUserRequest;
import com.example.domain.request.UserUpdateDTO;
import com.example.domain.request.UserUpdateProfileDTO;
import com.example.domain.response.ResUserDTO;
import com.example.repository.AuthUserRepository;
import com.example.service.AuthUserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthUserServiceImpl extends BaseServiceImpl<AuthUser, Long, CreateUserRequest, ResUserDTO> implements AuthUserService {

    @Value("${app.rabbitmq.send-routing-key-update}")
    private String sendRoutingKey;

    @Value("${app.rabbitmq.exchange}")
    private String exchangeName;

    private final AuthUserRepository authUserRepository;
    private final RabbitTemplate rabbitTemplate;

    public AuthUserServiceImpl(AuthUserRepository authUserRepository,
                               RabbitTemplate rabbitTemplate) {
        super(authUserRepository);
        this.authUserRepository = authUserRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public boolean isEmailExist(String email) {
        return authUserRepository.existsByEmail(email);
    }

    @Override
    public Optional<AuthUser> findByEmail(String email) {
        return authUserRepository.findByEmail(email);
    }

    @Override
    public void updateRefreshToken(String refreshToken, String username) {
        AuthUser currentUser = authUserRepository.findByEmail(username).orElse(null);
        if (currentUser != null) {
            currentUser.setRefreshToken(refreshToken);
            this.authUserRepository.save(currentUser);
        }
    }

    @Override
    public Optional<AuthUser> findByRefreshTokenAndEmail(String refreshToken, String email) {
        return this.authUserRepository.findByRefreshTokenAndEmail(refreshToken, email);
    }

    @Override
    public void updateUserToken(String token, String email) {
        AuthUser currentUser = this.findByEmail(email).orElse(null);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.authUserRepository.save(currentUser);
        }
    }

    @Override
    public ResUserDTO updateUser(AuthUser user, Role role, UserUpdateDTO dto) {
        String oldEmail = user.getEmail();

        user.setEmail(dto.getEmail());
        user.setRole(role);
        user.setEnabled(dto.isEnabled());

        AuthUser saved = authUserRepository.save(user);

        UserUpdateProfileDTO profileDTO = new UserUpdateProfileDTO();
        profileDTO.setName(dto.getName());
        profileDTO.setEmail(dto.getEmail());
        profileDTO.setPhone(dto.getPhone());
        profileDTO.setDateOfBirth(dto.getDateOfBirth());
        profileDTO.setGender(dto.getGender());
        profileDTO.setAddress(dto.getAddress());
        profileDTO.setRoleId(role.getId());
        profileDTO.setOldEmail(oldEmail);

        this.rabbitTemplate.convertAndSend(
                exchangeName, sendRoutingKey, profileDTO
        );

        ResUserDTO res = new ResUserDTO();
        res.setId(saved.getId());
        res.setName(dto.getName());
        res.setEmail(dto.getEmail());
        res.setRole(role.getCode());

        return res;
    }

    @Override
    public ResUserDTO updateUserStatus(AuthUser user) {
        user.setEnabled(!user.isEnabled());
        AuthUser saved = authUserRepository.save(user);

        ResUserDTO res = new ResUserDTO();
        res.setId(saved.getId());
        res.setEmail(saved.getEmail());
        res.setRole(saved.getRole().getCode());
        res.setEnabled(saved.isEnabled());

        return res;
    }

    @Override
    public boolean existsById(Long aLong) {
        return authUserRepository.existsById(aLong);
    }
}
