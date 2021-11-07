package com.epam.esm.service.dto.user;

import com.epam.esm.mapper.order.OrderDtoMapper;
import com.epam.esm.mapper.order.OrderDtoMapperImpl;
import com.epam.esm.mapper.user.UserDtoMapper;
import com.epam.esm.mapper.user.UserDtoMapperImpl;
import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.entity.user.UserService;
import com.epam.esm.service.entity.user.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDtoService implements UserDtoServiceI {
    private UserDtoMapper userDtoMapper;
    private UserServiceI service;
    private OrderDtoMapper orderDtoMapper;

    @Autowired
    public UserDtoService(UserDtoMapperImpl userDtoMapper,
                          UserService service,
                          OrderDtoMapperImpl orderDtoMapper
    ) {
        this.userDtoMapper = userDtoMapper;
        this.service = service;
        this.orderDtoMapper = orderDtoMapper;
    }

    @Override
    public UserDto read(long id) {
        return userDtoMapper.userToUserDto(service.read(id));
    }

    @Override
    public List<UserDto> read() {
        return userListToUserDtoList(service.read());
    }

    @Override
    public List<OrderDto> readOrdersByUserId(long id) {
        return orderListToOrderDtoList(service.readOrdersByIdUser(id));
    }

    @Override
    public UserDto bestUser() {
        return userDtoMapper.userToUserDto(service.bestUser());
    }

    @Override
    public List<UserDto> findPaginated(int page, int size) {
        return userListToUserDtoList(service.findPaginated(size, page));
    }

    private List<UserDto> userListToUserDtoList(List<User> userList) {
        return userList.stream()
                .map((user -> userDtoMapper.userToUserDto(user)))
                .collect(Collectors.toList());
    }

    private List<OrderDto> orderListToOrderDtoList(List<Order> orderList) {
        return orderList.stream()
                .map((order -> orderDtoMapper.orderToOrderDto(order)))
                .collect(Collectors.toList());
    }
}