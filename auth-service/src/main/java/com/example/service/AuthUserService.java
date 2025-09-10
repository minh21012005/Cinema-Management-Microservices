package com.example.service;

import com.example.entity.AuthUser;

import java.util.Optional;

public interface AuthUserService extends BaseService<AuthUser, Long>{
    boolean isEmailExist(String email);
    Optional<AuthUser> findByEmail(String email);
    void updateRefreshToken(String refreshToken, String username);
}
