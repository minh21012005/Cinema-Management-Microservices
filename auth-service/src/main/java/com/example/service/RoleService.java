package com.example.service;

import com.example.domain.entity.Role;
import com.example.domain.request.RoleReqDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.domain.response.RoleResponseDTO;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RoleService extends BaseService<Role, Long, RoleReqDTO, RoleResponseDTO>  {
    Optional<Role> findByCode(String code);
    RoleResponseDTO createRole(RoleReqDTO role) throws IdInvalidException;
    RoleResponseDTO updateRole(Role role, RoleReqDTO dto) throws IdInvalidException;
    ResultPaginationDTO fetchAllRolesWithPagination(String code, Pageable pageable);
    boolean existsByName(String name);
    boolean existsByCode(String code);
}
