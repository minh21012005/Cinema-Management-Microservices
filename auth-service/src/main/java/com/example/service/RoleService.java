package com.example.service;

import com.example.entity.Role;
import com.example.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findById(Long id){
        return this.roleRepository.findById(id).orElse(null);
    }
    public Role findByName(String name){
        return this.roleRepository.findByName(name);
    }
}
