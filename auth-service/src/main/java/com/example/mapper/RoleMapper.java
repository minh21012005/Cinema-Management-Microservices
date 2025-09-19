package com.example.mapper;

import com.example.domain.entity.Permission;
import com.example.domain.entity.Role;
import com.example.domain.request.RoleReqDTO;
import com.example.domain.response.RoleResponseDTO;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper extends BaseMapper<Role, RoleReqDTO, RoleResponseDTO> {

    default Role toEntity(RoleReqDTO dto) {
        if (dto == null) return null;
        Role role = new Role();
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        role.setActive(dto.isActive());
        role.setCode(dto.getCode());
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
        dto.setCode(entity.getCode());
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

    default String map(Permission permission) {
        return permission.getCode();
    }

    // Map List<Permission> -> List<String>
    default List<String> map(List<Permission> permissions) {
        if (permissions == null) return null;
        return permissions.stream()
                .map(Permission::getCode) // hoặc getCode()
                .collect(Collectors.toList());
    }
}


