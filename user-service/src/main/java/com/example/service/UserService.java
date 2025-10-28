package com.example.service;

import com.example.domain.entity.User;
import com.example.domain.request.CreateUserRequest;
import com.example.domain.request.UserUpdateDTO;
import com.example.domain.response.ResUserDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService extends BaseService<User,Long, CreateUserRequest, ResUserDTO> {
    boolean isPhoneExist(String phone);
    ResultPaginationDTO fetchAllUser(String emailFilter, Long roleFilter, Pageable pageable);
    ResUserDTO createUser(CreateUserRequest dto) throws IdInvalidException;
    ResUserDTO updateUser(User user, UserUpdateDTO dto) throws IdInvalidException;
    Optional<User> findByEmail(String email);
    String getNameByEmail(String email) throws IdInvalidException;
    Map<Long, String > getNamesByIds(List<Long> ids);
    Long getNewUsersCount();
}
