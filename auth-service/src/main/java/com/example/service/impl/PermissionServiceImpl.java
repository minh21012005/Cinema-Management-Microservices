package com.example.service.impl;

import com.example.domain.entity.Permission;
import com.example.domain.request.PermissionReqDTO;
import com.example.domain.response.PermissionResDTO;
import com.example.mapper.PermissionMapper;
import com.example.repository.PermissionRepository;
import com.example.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl
        extends BaseServiceImpl<Permission, Long, PermissionReqDTO, PermissionResDTO>
        implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    protected PermissionServiceImpl(PermissionRepository permissionRepository,
                                    PermissionMapper permissionMapper) {
        super(permissionRepository);
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public List<PermissionResDTO> getActivePermissions() {
        return permissionRepository.findByActiveTrue()
                .stream()
                .map(permissionMapper::toDto)
                .toList();
    }
}
