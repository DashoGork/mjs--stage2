package com.epam.esm.service.dto.user;

import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.dto.UserDto;

import java.util.List;

public interface UserDtoServiceI {
    UserDto read(long id);

    List<UserDto> read();

    List<OrderDto> readOrdersByUserId(long id);

    List<UserDto> findPaginated(int page, int size);

    UserDto create(UserDto userDto);

    UserDto read(String name);
}