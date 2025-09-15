package com.example.mapper;

import com.example.domain.entity.Permission;
import com.example.domain.entity.Role;
import com.example.domain.request.RoleCreateDTO;
import com.example.domain.response.RoleResponseDTO;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    default Role toEntity(RoleCreateDTO dto) {
        if (dto == null) return null;
        Role role = new Role();
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        role.setActive(dto.isActive());
        // permissions sẽ set sau trong service
        return role;
    }

    default RoleResponseDTO toDTO(Role entity) {
        if (entity == null) return null;
        RoleResponseDTO dto = new RoleResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setActive(entity.isActive());
        // map permissions sang tên
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
}


