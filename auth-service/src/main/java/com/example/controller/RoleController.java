package com.example.controller;

import com.example.domain.entity.Role;
import com.example.service.BaseService;
import com.example.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController extends BaseController<Role, Long> {
    private final RoleService roleService;

    public RoleController(BaseService<Role, Long> service, RoleService roleService) {
        super(service);
        this.roleService = roleService;
    }

    @Override
    @PreAuthorize("hasPermission(null, 'ROLE_CREATE')")
    public ResponseEntity<Role> create(@Valid @RequestBody Role role) {
        return super.create(role);
    }
}
