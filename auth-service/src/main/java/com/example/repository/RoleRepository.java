package com.example.repository;

import com.example.domain.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends BaseRepository<Role, Long> {
    Optional<Role> findByCode(String code);
    boolean existsByName(String name);
}
