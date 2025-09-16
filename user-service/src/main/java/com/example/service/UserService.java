package com.example.service;

import com.example.domain.User;
import com.example.domain.request.CreateUserRequest;
import com.example.domain.response.ResUserDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;

public interface UserService extends BaseService<User,Long> {
    boolean isPhoneExist(String phone);
    ResultPaginationDTO fetchAllUser(String emailFilter, String roleFilter, Pageable pageable);
    ResUserDTO createUser(CreateUserRequest dto) throws IdInvalidException;
}
