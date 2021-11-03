package com.epam.esm.mapper.user;

import com.epam.esm.model.dto.UserDto;
import com.epam.esm.model.entity.User;


public interface UserDtoMapper {
    User userDtoToUser(UserDto userDto);

    UserDto userToUserDto(User user);
}