package com.example.service.impl;

import com.example.domain.entity.Permission;
import com.example.domain.request.PermissionReqDTO;
import com.example.domain.response.PermissionResDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.mapper.PermissionMapper;
import com.example.repository.PermissionRepository;
import com.example.service.PermissionService;
import com.example.service.specification.PermissionSpecification;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl
        extends BaseServiceImpl<Permission, Long, PermissionReqDTO, PermissionResDTO>
        implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    protected PermissionServiceImpl(PermissionRepository permissionRepository,
                                    PermissionMapper permissionMapper) {
        super(permissionRepository);
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public List<PermissionResDTO> getActivePermissions() {
        return permissionRepository.findByActiveTrue()
                .stream()
                .map(permissionMapper::toDto)
                .toList();
    }

    @Override
    public ResultPaginationDTO fetchAllPermissionsWithPagination(String module, Pageable pageable) {
        Page<Permission> pagePermission = this.permissionRepository.findAll(
                PermissionSpecification.findPermissionsWithFilters(module), pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber());
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pagePermission.getTotalPages());
        mt.setTotal(pagePermission.getTotalElements());

        rs.setMeta(mt);

        // map entity -> dto
        List<PermissionResDTO> listPermission = pagePermission.getContent()
                .stream()
                .map(permissionMapper::toDto)
                .collect(Collectors.toList());

        rs.setResult(listPermission);

        return rs;
    }

    @Override
    public PermissionResDTO createPermission(PermissionReqDTO dto) throws IdInvalidException {
        // 1. Validate code duy nhất
        if (permissionRepository.existsByCode(dto.getCode())) {
            throw new IdInvalidException("Permission code already exists");
        }

        // 2. Validate name duy nhất (nếu bạn muốn constraint thêm)
        if (permissionRepository.existsByName(dto.getName())) {
            throw new IdInvalidException("Permission name already exists");
        }

        // 3. Map từ DTO sang Entity
        Permission entity = permissionMapper.toEntity(dto);

        // 4. Save DB
        Permission saved = permissionRepository.save(entity);

        // 5. Map sang ResDTO để trả về
        return permissionMapper.toDto(saved);
    }

}
