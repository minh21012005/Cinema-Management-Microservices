package com.example.service.impl;

import com.example.domain.entity.Permission;
import com.example.domain.entity.Role;
import com.example.domain.request.RoleReqDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.domain.response.RoleResponseDTO;
import com.example.mapper.RoleMapper;
import com.example.repository.PermissionRepository;
import com.example.repository.RoleRepository;
import com.example.service.RoleService;
import com.example.service.specification.RoleSpecification;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl
        extends BaseServiceImpl<Role, Long, RoleReqDTO, RoleResponseDTO>
        implements RoleService {

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
    public Optional<Role> findByCode(String code) {
        return roleRepository.findByCode(code);
    }

    @Override
    public RoleResponseDTO createRole(RoleReqDTO role) throws IdInvalidException {
        if(roleRepository.existsByCode(role.getCode())){
            throw new IdInvalidException("Role code already exists");
        }

        if(roleRepository.existsByName(role.getName())){
            throw new IdInvalidException("Role name already exists");
        }

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
    public RoleResponseDTO updateRole(Role entity, RoleReqDTO dto) throws IdInvalidException {
        // Update các trường cơ bản
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.isActive());
        entity.setCode(dto.getCode());

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
    public ResultPaginationDTO fetchAllRolesWithPagination(String code, Pageable pageable) {
        Page<Role> pageRole = this.roleRepository.findAll(
                RoleSpecification.findRoleWithFilters(code), pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber());
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageRole.getTotalPages());
        mt.setTotal(pageRole.getTotalElements());

        rs.setMeta(mt);

        // map entity -> dto
        List<RoleResponseDTO> listRole = pageRole.getContent()
                .stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());

        rs.setResult(listRole);

        return rs;
    }

    @Override
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    @Override
    public boolean existsByCode(String code) {
        return roleRepository.existsByCode(code);
    }

    @Override
    public boolean existsById(Long aLong) {
        return roleRepository.existsById(aLong);
    }
}
