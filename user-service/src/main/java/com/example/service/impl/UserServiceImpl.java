package com.example.service.impl;

import com.example.domain.User;
import com.example.domain.response.ResUserDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import com.example.service.specification.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl
        extends BaseServiceImpl<User, Long>
        implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Override
    public boolean isPhoneExist(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public ResultPaginationDTO fetchAllUser(String emailFilter, String roleFilter, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(
                UserSpecification.findUsersWithFilters(emailFilter, roleFilter), pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber());
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
        res.setRole(user.getRole());
        return res;
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}

