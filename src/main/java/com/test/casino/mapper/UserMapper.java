package com.test.casino.mapper;

import com.test.casino.dtos.UserDto;
import com.test.casino.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", source = "userDto.id")
    @Mapping(target = "username", source = "userDto.username")
    @Mapping(target = "balance", source = "userDto.balance")
    User toUser(UserDto userDto);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "password", source = "user.password")
    @Mapping(target = "balance", source = "user.balance")
    UserDto toUserDto(User user);
}
