package com.example.service;

import com.example.domain.entity.User;
import com.example.domain.request.CreateUserRequest;
import com.example.domain.request.UserUpdateDTO;
import com.example.domain.response.ResUserDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService extends BaseService<User,Long, CreateUserRequest, ResUserDTO> {
    boolean isPhoneExist(String phone);
    ResultPaginationDTO fetchAllUser(String emailFilter, String roleFilter, Pageable pageable);
    ResUserDTO createUser(CreateUserRequest dto) throws IdInvalidException;
    ResUserDTO updateUser(User user, UserUpdateDTO dto);
    Optional<User> findByEmail(String email);
}
