package com.test.casino.service;


import com.test.casino.dtos.UserDto;
import com.test.casino.model.entity.User;

import java.math.BigDecimal;

public interface UserService {

    UserDto save(UserDto userDto);
    UserDto getById(Integer id);
    UserDto updateBalance(Integer userId, BigDecimal amount);
    User login(UserDto userDto);
}
