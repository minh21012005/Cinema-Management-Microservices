package com.example.service;

import com.example.domain.entity.Role;
import java.util.Optional;

public interface RoleService extends BaseService<Role, Long>  {
    Optional<Role> findByName(String name);
}
