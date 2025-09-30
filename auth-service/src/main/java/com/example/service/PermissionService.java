package com.example.service;

import com.example.domain.entity.Permission;
import com.example.domain.request.PermissionReqDTO;
import com.example.domain.response.PermissionResDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PermissionService extends BaseService<Permission, Long, PermissionReqDTO, PermissionResDTO>  {
    List<PermissionResDTO> getActivePermissions();
    ResultPaginationDTO fetchAllPermissionsWithPagination(String module, Pageable pageable);
    PermissionResDTO createPermission(PermissionReqDTO dto) throws IdInvalidException;
}
