package com.example.service.impl;

import com.example.domain.entity.Permission;
import com.example.domain.entity.Role;
import com.example.domain.request.RoleCreateDTO;
import com.example.domain.request.RoleUpdateDTO;
import com.example.domain.response.RoleResponseDTO;
import com.example.mapper.RoleMapper;
import com.example.repository.PermissionRepository;
import com.example.repository.RoleRepository;
import com.example.service.RoleService;
import com.example.util.error.IdInvalidException;
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
    public RoleResponseDTO createRole(RoleCreateDTO role) {
        Role entity = roleMapper.toEntity(role);

        // Lấy thực Permission từ DB theo permissionIds
        if (role.getPermissionIds() != null && !role.getPermissionIds().isEmpty()) {
            List<Permission> permissions = permissionRepository.findAllById(role.getPermissionIds());
            entity.setPermissions(permissions);
        }
        else {
            entity.setPermissions(new ArrayList<>());
        }
        Role saved = roleRepository.save(entity);

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
    public RoleResponseDTO updateRole(RoleUpdateDTO dto) throws IdInvalidException {
        Role entity = roleRepository.findById(dto.getId())
                .orElseThrow(() -> new IdInvalidException("Role not found"));

        // Check trùng tên nếu đổi
        if (!entity.getName().equals(dto.getName()) &&
                roleRepository.existsByName(dto.getName())) {
            throw new IdInvalidException("Role name already exists");
        }

        // Update các trường cơ bản
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.isActive());

        // Update permissions nếu có
        if (dto.getPermissionIds() != null && !dto.getPermissionIds().isEmpty()) {
            List<Permission> permissions = permissionRepository.findAllById(dto.getPermissionIds());
            entity.setPermissions(permissions);
        } else {
            entity.setPermissions(new ArrayList<>());
        }

        Role saved = roleRepository.save(entity);

        RoleResponseDTO response = roleMapper.toDTO(saved);
        if (saved.getPermissions() != null) {
            response.setPermissions(saved.getPermissions()
                    .stream()
                    .map(Permission::getName)
                    .collect(Collectors.toList()));
        } else {
            response.setPermissions(new ArrayList<>());
        }

        return response;
    }

    @Override
    public boolean existsById(Long id) {
        return roleRepository.existsById(id);
    }
}
