package com.example.service;



import com.example.domain.entity.Role;
import com.example.domain.entity.User;
import com.example.domain.request.CreateUserRequest;
import com.example.domain.response.ResCreateUserDTO;
import com.example.domain.response.ResUserDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.repository.UserRepository;
import com.example.service.specification.UserSpecification;
import com.example.util.constant.GenderEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public boolean isPhoneExist(String phone) {
        return this.userRepository.existsByPhone(phone);
    }

    public User handleCreateUser(User user) {
        Role role = this.roleService.fetchById(4);
        user.setRole(role);
        return this.userRepository.save(user);
    }

    public User handleCreateUserForManager(CreateUserRequest dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPassword(dto.getPassword());
        user.setAddress(dto.getAddress());
        user.setGender(GenderEnum.valueOf(dto.getGender()));
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setRole(this.roleService.fetchById(dto.getRoleId()));
        return this.userRepository.save(user);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();

        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setDateOfBirth(user.getDateOfBirth());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        res.setPhone(user.getPhone());

        return res;
    }

    public ResultPaginationDTO fetchAllUser(String emailFilter, String roleFilter, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(
                UserSpecification.findUsersWithFilters(emailFilter, roleFilter), pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResUserDTO> listUser = pageUser.getContent()
                .stream().map(item -> this.convertToResUserDTO(item))
                .collect(Collectors.toList());

        rs.setResult(listUser);

        return rs;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        res.setDateOfBirth(user.getDateOfBirth());
        res.setPhone(user.getPhone());
        res.setRole(user.getRole());
        res.setEnabled(user.isEnabled());
        return res;
    }

    public ResUserDTO changeStatusOfUser(long id) {
        ResUserDTO res = new ResUserDTO();
        User user = this.userRepository.findById(id).orElse(null);
        if (user != null) {
            if (user.isEnabled()) {
                user.setEnabled(false);
            } else {
                user.setEnabled(true);
            }
            this.userRepository.save(user);
            res = this.convertToResUserDTO(user);
        }
        return res;
    }

    public ResUserDTO fetchUserById(long id) {
        User user = this.userRepository.findById(id).orElse(null);
        if (user != null) {
            return convertToResUserDTO(user);
        }
        return null;
    }
}
