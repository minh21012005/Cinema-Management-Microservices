package com.example.service.impl;

import com.example.domain.entity.Permission;
import com.example.domain.entity.Role;
import com.example.domain.request.RoleRequestDTO;
import com.example.domain.response.RoleResponseDTO;
import com.example.mapper.RoleMapper;
import com.example.repository.PermissionRepository;
import com.example.repository.RoleRepository;
import com.example.service.RoleService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role, Long> implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PermissionRepository permissionRepository;

    protected RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper,
                              PermissionRepository permissionRepository) {
        super(roleRepository);
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public RoleResponseDTO createRole(RoleRequestDTO role) {
        Role entity = roleMapper.toEntity(role);

        // Lấy thực Permission từ DB theo permissionIds
        if (role.getPermissionIds() != null && !role.getPermissionIds().isEmpty()) {
            List<Permission> permissions = permissionRepository.findAllById(role.getPermissionIds());
            entity.setPermissions(permissions);
        }
        else {
            entity.setPermissions(new ArrayList<>());
        }
        System.out.println(entity); // trước save
        Role saved = roleRepository.save(entity);
        System.out.println(saved);

        // Tạo DTO trả về và convert permission sang name thủ công
        RoleResponseDTO dto = roleMapper.toDTO(saved);
        if (entity.getPermissions() != null) {
            dto.setPermissions(entity.getPermissions()
                    .stream()
                    .map(Permission::getName)
                    .collect(Collectors.toList()));
        } else {
            dto.setPermissions(new ArrayList<>());
        }

        return dto;
    }

    @Override
    public boolean existsById(Long id) {
        return roleRepository.existsById(id);
    }
}
