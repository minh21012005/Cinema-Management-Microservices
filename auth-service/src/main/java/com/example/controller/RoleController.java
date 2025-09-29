package com.example.controller;

import com.example.domain.entity.Role;
import com.example.domain.request.RoleReqDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.domain.response.RoleResponseDTO;
import com.example.mapper.RoleMapper;
import com.example.service.RoleService;
import com.example.util.annotation.ApiMessage;
import com.example.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController extends BaseController<Role, Long, RoleReqDTO, RoleResponseDTO> {
    private final RoleService roleService;

    public RoleController(RoleService roleService, RoleMapper roleMapper) {
        super(roleService, roleMapper);
        this.roleService = roleService;
    }

    @Override
    @PreAuthorize("hasPermission(null, 'ROLE_CREATE')")
    public ResponseEntity<RoleResponseDTO> create(@Valid @RequestBody RoleReqDTO role) throws IdInvalidException {
        return ResponseEntity.ok(roleService.createRole(role));
    }

    @GetMapping("/all")
    @ApiMessage("Fetched all roles")
    @PreAuthorize("hasPermission(null, 'ROLE_VIEW')")
    public ResponseEntity<ResultPaginationDTO> fetchAllPagination(
            @RequestParam(name = "name", required = false) String name,
            Pageable pageable) {
        return ResponseEntity.ok(roleService.fetchAllRolesWithPagination(name, pageable));
    }

    @Override
    @PreAuthorize("hasPermission(null, 'ROLE_VIEW_ALL')")
    public ResponseEntity<List<Role>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasPermission(null, 'ROLE_VIEW')")
    public ResponseEntity<Role> getById(@PathVariable("id") Long id) throws IdInvalidException {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasPermission(null, 'ROLE_UPDATE')")
    public ResponseEntity<RoleResponseDTO> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody RoleReqDTO dto) throws IdInvalidException {
        Role entity = roleService.findById(id)
                .orElseThrow(() -> new IdInvalidException("Role not found"));

        // Check trùng tên nếu đổi
        if (!entity.getName().equals(dto.getName()) &&
                roleService.existsByName(dto.getName())) {
            throw new IdInvalidException("Role name already exists");
        }

        if (!entity.getCode().equals(dto.getCode()) &&
                roleService.existsByCode(dto.getCode())) {
            throw new IdInvalidException("Role code already exists");
        }
        return ResponseEntity.ok(roleService.updateRole(entity, dto));
    }

    @GetMapping("/code")
    public String getRoleCode(@RequestParam("id") Long id) throws IdInvalidException {
        return roleService.findById(id).orElseThrow(
                () -> new IdInvalidException("Id role is Invalid!")).getCode();
    }

    @Override
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws IdInvalidException {
        throw new UnsupportedOperationException("Không được phép xóa Role");
    }
}
