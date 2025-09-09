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
}
