package com.example.repository;

import com.example.domain.entity.Permission;

import java.util.List;

public interface PermissionRepository extends BaseRepository<Permission, Long>{
    List<Permission> findByActiveTrue();
}
