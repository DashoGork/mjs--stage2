package com.epam.esm.dao.user;

import com.epam.esm.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDaoI {
    List<User> read();
    Optional<User> read(long id);
    List<User> read(int offset, int limit);
    void create(User user);

    Optional<User> read(String name);
}
