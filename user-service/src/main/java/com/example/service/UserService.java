package com.example.service;

import com.example.domain.User;

public interface UserService extends BaseService<User,Long> {
    boolean isPhoneExist(String phone);
}
