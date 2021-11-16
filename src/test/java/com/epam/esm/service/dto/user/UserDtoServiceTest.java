package com.epam.esm.service.dto.user;

import com.epam.esm.mapper.order.OrderDtoMapperImpl;
import com.epam.esm.mapper.user.UserDtoMapperImpl;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.entity.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserDtoServiceTest {
    @Mock
    private UserService service;

    private UserDtoMapperImpl userDtoMapper;
    private OrderDtoMapperImpl orderDtoMapper;
    private UserDtoService dtoService;
    private User user;
    private UserDto userDto;

    @Before
    public void setUp() throws Exception {
        userDtoMapper = new UserDtoMapperImpl();
        orderDtoMapper = new OrderDtoMapperImpl();
        dtoService = new UserDtoService(userDtoMapper, service, orderDtoMapper);
        user = new User();
        user.setName("name");
        user.setId(1l);
        userDto = userDtoMapper.userToUserDto(user);
    }

    @Test
    public void readById() {
        when(service.read(1l)).thenReturn(user);
        assertTrue(dtoService.read(1l).getName().equals(userDto.getName()));
        verify(service).read(1l);
    }

    @Test
    public void read() {
        List<User> list = new ArrayList<>();
        list.add(user);
        when(service.read()).thenReturn(list);
        assertTrue(dtoService.read().size() == 1);
        verify(service).read();
    }
}