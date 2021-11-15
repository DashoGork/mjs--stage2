package com.epam.esm.service.entity.user;

import com.epam.esm.dao.user.UserDaoI;
import com.epam.esm.dao.user.impl.UserDao;
import com.epam.esm.exceptions.BaseNotFoundException;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.entity.PaginationCalcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements UserServiceI, PaginationCalcService {
    private UserDaoI userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> read() {
        return userDao.read();
    }

    public User read(long id) {
        Optional<User> user = userDao.read(id);
        if (!user.isPresent()) {
            throw new BaseNotFoundException("User wasn't" +
                    " found. id =" + id);
        } else {
            return user.get();
        }
    }

    public List<Order> readOrdersByIdUser(long id) {
        return read(id).getOrders();
    }

    @Override
    public List<User> findPaginated(int size, int page) {
        Map<String, Integer> indexes = paginate(read().size(), size, page);
        return userDao.read(indexes.get("offset"), indexes.get("limit"));
    }
}