package com.example.repository;

import com.example.domain.entity.Permission;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PermissionRepository extends BaseRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    List<Permission> findByActiveTrue();
    boolean existsByCode(String code);
    boolean existsByName(String name);
}
