package com.example.service;

import com.example.domain.entity.AuthUser;
import com.example.domain.entity.Role;
import com.example.domain.request.UserUpdateDTO;
import com.example.domain.response.ResUserDTO;

import java.util.Optional;

public interface AuthUserService extends BaseService<AuthUser, Long>{
    boolean isEmailExist(String email);
    Optional<AuthUser> findByEmail(String email);
    void updateRefreshToken(String refreshToken, String username);
    Optional<AuthUser> findByRefreshTokenAndEmail(String refreshToken, String email);
    void updateUserToken(String token, String email);
    ResUserDTO updateUser(AuthUser user, Role role, UserUpdateDTO dto);
    ResUserDTO updateUserStatus(AuthUser user);
}
