package com.example.service.impl;

import com.example.domain.entity.Permission;
import com.example.domain.request.PermissionReqDTO;
import com.example.domain.response.PermissionResDTO;
import com.example.repository.PermissionRepository;
import com.example.service.PermissionService;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl
        extends BaseServiceImpl<Permission, Long, PermissionReqDTO, PermissionResDTO>
        implements PermissionService {

    private final PermissionRepository permissionRepository;

    protected PermissionServiceImpl(PermissionRepository permissionRepository) {
        super(permissionRepository);
        this.permissionRepository = permissionRepository;
    }


}
