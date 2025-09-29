package com.example.repository;

import com.example.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends BaseRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    Optional<Role> findByCode(String code);
    boolean existsByName(String name);
    boolean existsByCode(String code);
}
