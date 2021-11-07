package com.epam.esm.service.entity.user;

import com.epam.esm.dao.order.OrderDao;
import com.epam.esm.dao.user.UserDao;
import com.epam.esm.exceptions.BaseNotFoundException;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.entity.PaginationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserServiceI, PaginationService<User> {
    private UserDao userDao;
    private OrderDao orderDao;

    @Autowired
    public UserService(OrderDao orderDao,
                       UserDao userDao) {
        this.orderDao = orderDao;
        this.userDao = userDao;
    }

    public List<User> read() {
        return userDao.findAll();
    }

    public User read(long id) {
        if (id > 0) {
            Optional<User> user = userDao.findById(id);
            if (!user.isPresent()) {
                throw new BaseNotFoundException("User wasn't" +
                        " found. id =" + id);
            } else {
                return user.get();
            }
        } else {
            throw new InvalidParameterException("invalid id. id = " + id);
        }
    }

    public List<Order> readOrdersByIdUser(long id) {
        return read(id).getOrders();
    }

    @Override
    public List<User> findPaginated(int size, int page) {
        return paginate(read(), size, page);
    }

    @Override
    public User bestUser() {
        return read(orderDao.findTopUserByPrice().get(0));
    }
}