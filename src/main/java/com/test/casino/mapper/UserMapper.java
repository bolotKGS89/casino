package com.test.casino.mapper;

import com.test.casino.dtos.UserDto;
import com.test.casino.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "balance", source = "balance")
    User toUser(UserDto userDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "balance", source = "balance")
    UserDto toUserDto(User user);
}
