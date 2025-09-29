package com.example.controller;

import com.example.domain.entity.Permission;
import com.example.domain.request.PermissionReqDTO;
import com.example.domain.response.PermissionResDTO;
import com.example.mapper.PermissionMapper;
import com.example.service.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController extends BaseController<Permission, Long, PermissionReqDTO, PermissionResDTO> {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService, PermissionMapper permissionMapper) {
        super(permissionService, permissionMapper);
        this.permissionService = permissionService;
    }

    @GetMapping("/active")
    @PreAuthorize("hasPermission(null, 'PERMISSION_VIEW')")
    public ResponseEntity<List<PermissionResDTO>> getActivePermissions() {
        return ResponseEntity.ok(permissionService.getActivePermissions());
    }
}
