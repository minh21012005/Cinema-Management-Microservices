package com.example.controller;

import com.example.domain.entity.RoleDTO;
import com.example.domain.entity.Role;
import com.example.service.BaseService;
import com.example.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

//    @PostMapping
//    public ResponseEntity<RoleDTO> createRole(@RequestBody Role role) {
//        RoleDTO roleDTO = roleService.createRole(role);
//        RoleDTO roleDTO = roleService.createDefaultRoles();
//        return ResponseEntity.ok(roleDTO);
//    }
}
