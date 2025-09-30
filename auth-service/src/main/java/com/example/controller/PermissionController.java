package com.example.controller;

import com.example.domain.entity.Permission;
import com.example.domain.request.PermissionReqDTO;
import com.example.domain.response.PermissionResDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.mapper.PermissionMapper;
import com.example.service.PermissionService;
import com.example.util.annotation.ApiMessage;
import com.example.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController
        extends BaseController<Permission, Long, PermissionReqDTO, PermissionResDTO> {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService, PermissionMapper permissionMapper) {
        super(permissionService, permissionMapper);
        this.permissionService = permissionService;
    }

    @GetMapping("/all")
    @ApiMessage("Fetched all permissions")
    @PreAuthorize("hasPermission(null, 'PERMISSION_VIEW')")
    public ResponseEntity<ResultPaginationDTO> fetchAllPagination(
            @RequestParam(name = "module", required = false) String module,
            @PageableDefault(sort = {"module", "id"}) Pageable pageable) {
        return ResponseEntity.ok(permissionService.fetchAllPermissionsWithPagination(module, pageable));
    }

    @Override
    @PreAuthorize("hasPermission(null, 'PERMISSION_CREATE')")
    public ResponseEntity<PermissionResDTO> create(@Valid @RequestBody PermissionReqDTO dto)
            throws IdInvalidException {
        return ResponseEntity.ok(permissionService.createPermission(dto));
    }

    @GetMapping("/active")
    @PreAuthorize("hasPermission(null, 'PERMISSION_VIEW')")
    public ResponseEntity<List<PermissionResDTO>> getActivePermissions() {
        return ResponseEntity.ok(permissionService.getActivePermissions());
    }
}
