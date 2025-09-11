package com.example.service.impl;

import com.example.domain.entity.AuthUser;
import com.example.repository.AuthUserRepository;
import com.example.service.AuthUserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthUserServiceImpl extends BaseServiceImpl<AuthUser, Long> implements AuthUserService {
    private final AuthUserRepository authUserRepository;

    public AuthUserServiceImpl(AuthUserRepository authUserRepository) {
        super(authUserRepository);
        this.authUserRepository = authUserRepository;
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
}
