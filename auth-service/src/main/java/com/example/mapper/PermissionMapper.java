package com.example.mapper;

import com.example.domain.entity.Permission;
import com.example.domain.request.PermissionReqDTO;
import com.example.domain.response.PermissionResDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper extends BaseMapper<Permission, PermissionReqDTO, PermissionResDTO> {
    @Override
    PermissionResDTO toDto(Permission entity);

    @Override
    Permission toEntity(PermissionReqDTO dto);
}
