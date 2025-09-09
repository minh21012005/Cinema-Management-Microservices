package com.example.service;

import com.example.entity.AuthRole;
import com.example.repository.AuthRoleRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthRoleService {
    private final AuthRoleRepository authRoleRepository;

    public AuthRoleService(AuthRoleRepository authRoleRepository) {
        this.authRoleRepository = authRoleRepository;
    }

    public AuthRole findById(Long id){
        return this.authRoleRepository.findById(id).orElse(null);
    }
}
