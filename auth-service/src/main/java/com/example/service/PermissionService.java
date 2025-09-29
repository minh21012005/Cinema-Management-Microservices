package com.example.service;

import com.example.domain.entity.Permission;
import com.example.domain.request.PermissionReqDTO;
import com.example.domain.response.PermissionResDTO;

import java.util.List;

public interface PermissionService extends BaseService<Permission, Long, PermissionReqDTO, PermissionResDTO>  {
    List<PermissionResDTO> getActivePermissions();
}
