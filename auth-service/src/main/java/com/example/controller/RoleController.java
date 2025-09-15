package com.example.controller;

import com.example.domain.entity.Role;
import com.example.domain.request.RoleCreateDTO;
import com.example.domain.request.RoleUpdateDTO;
import com.example.domain.response.RoleResponseDTO;
import com.example.service.BaseService;
import com.example.service.RoleService;
import com.example.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController extends BaseController<Role, Long> {
    private final RoleService roleService;

    public RoleController(BaseService<Role, Long> service, RoleService roleService) {
        super(service);
        this.roleService = roleService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasPermission(null, 'ROLE_CREATE')")
    public ResponseEntity<RoleResponseDTO> create(@Valid @RequestBody RoleCreateDTO role) {
        return ResponseEntity.ok(roleService.createRole(role));
    }

    @Override
    @PreAuthorize("hasPermission(null, 'ROLE_VIEW_ALL')")
    public ResponseEntity<List<Role>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasPermission(null, 'ROLE_VIEW')")
    public ResponseEntity<Role> getById(@PathVariable("id") Long id) {
        return super.getById(id);
    }

    @PutMapping()
    @PreAuthorize("hasPermission(null, 'ROLE_UPDATE')")
    public ResponseEntity<RoleResponseDTO> update(
            @Valid @RequestBody RoleUpdateDTO dto) throws IdInvalidException {
        return ResponseEntity.ok(roleService.updateRole(dto));
    }

}
