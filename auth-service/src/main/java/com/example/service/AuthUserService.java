package com.example.service;

import com.example.entity.AuthUser;
import com.example.repository.AuthUserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService {
    private final AuthUserRepository authUserRepository;

    public AuthUserService(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    public AuthUser saveUser(AuthUser authUser){
        return this.authUserRepository.save(authUser);
    }

    public AuthUser handleGetUserByUsername(String username) {
        return this.authUserRepository.findByEmail(username).orElse(null);
    }

    public boolean isEmailExist(String email) {
        return this.authUserRepository.existsByEmail(email);
    }

    public void updateUserToken(String token, String email) {
        AuthUser currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.authUserRepository.save(currentUser);
        }
    }
}
