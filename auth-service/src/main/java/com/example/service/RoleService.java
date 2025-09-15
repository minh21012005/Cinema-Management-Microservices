package com.example.service;

import com.example.domain.entity.Role;
import com.example.domain.request.RoleRequestDTO;
import com.example.domain.response.RoleResponseDTO;

import java.util.Optional;

public interface RoleService extends BaseService<Role, Long>  {
    Optional<Role> findByName(String name);
    RoleResponseDTO createRole(RoleRequestDTO role);
}
