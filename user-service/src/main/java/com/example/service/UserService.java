package com.example.service;

import com.example.domain.entity.RoleDTO;
import com.example.entity.User;
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

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public boolean isPhoneExist(String phone) {
        return this.userRepository.existsByPhone(phone);
    }

    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

}
