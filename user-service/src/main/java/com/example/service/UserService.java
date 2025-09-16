package com.example.service;

import com.example.domain.User;
import com.example.domain.response.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;

public interface UserService extends BaseService<User,Long> {
    boolean isPhoneExist(String phone);
    ResultPaginationDTO fetchAllUser(String emailFilter, String roleFilter, Pageable pageable);
}
