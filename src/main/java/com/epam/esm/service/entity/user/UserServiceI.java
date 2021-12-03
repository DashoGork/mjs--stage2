package com.epam.esm.service.entity.user;

import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;

import java.util.List;

public interface UserServiceI {
    User read(long id);
    List<User> read();

    User read(String name);

    List<Order> readOrdersByIdUser(long id);

    List<User> findPaginated(int size, int page);

    User create(User user);
}