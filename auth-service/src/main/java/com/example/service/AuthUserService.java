package com.example.service;

import com.example.domain.entity.AuthUser;
import com.example.domain.entity.Role;
import com.example.domain.request.CreateUserRequest;
import com.example.domain.request.UserUpdateDTO;
import com.example.domain.request.VerifyOtpRequest;
import com.example.domain.response.ResUserDTO;
import com.example.util.error.IdInvalidException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;

public interface AuthUserService extends BaseService<AuthUser, Long, CreateUserRequest, ResUserDTO>{
    boolean isEmailExist(String email);
    Optional<AuthUser> findByEmail(String email);
    void updateRefreshToken(String refreshToken, String username);
    Optional<AuthUser> findByRefreshTokenAndEmail(String refreshToken, String email);
    void updateUserToken(String token, String email);
    ResUserDTO updateUser(AuthUser user, Role role, UserUpdateDTO dto);
    ResUserDTO updateUserStatus(AuthUser user);
    ResUserDTO registerVerify(VerifyOtpRequest req) throws IdInvalidException, JsonProcessingException;
    AuthUser registerOAuthUser(String email, String name);
}
