package com.example.service;


import com.example.domain.entity.Role;
import com.example.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(
            RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role fetchById(long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if (roleOptional.isPresent())
            return roleOptional.get();
        return null;
    }

    public List<Role> fetchAllRole() {
        return this.roleRepository.findAll();
    }
}
