package com.example.service;

import com.example.domain.entity.Role;
import com.example.domain.request.RoleCreateDTO;
import com.example.domain.request.RoleUpdateDTO;
import com.example.domain.response.RoleResponseDTO;
import com.example.util.error.IdInvalidException;

import java.util.Optional;

public interface RoleService extends BaseService<Role, Long>  {
    Optional<Role> findByName(String name);
    RoleResponseDTO createRole(RoleCreateDTO role);
    RoleResponseDTO updateRole(RoleUpdateDTO role) throws IdInvalidException;
}
