package com.epam.esm.service.entity.order;

import com.epam.esm.dao.order.OrderDao;
import com.epam.esm.dao.user.UserDao;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.entity.user.UserService;
import com.epam.esm.service.entity.user.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;

@Service
public class OrderService implements OrderServiceI {
    private OrderDao orderDao;
    private UserDao userDao;
    private UserServiceI userService;

    @Autowired
    public OrderService(OrderDao orderDao,
                        UserDao userDao,
                        UserService userService) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.userService = userService;
    }

    @Transactional
    public Order addOrder(Order order) {
        order.setPrice();
        order.setTimeOfPurchase();
        User user = userService.read(order.getUserId());
        if (user.getPurse() >= order.getPrice()) {
            user.setPurse(user.getPurse() - order.getPrice());
            userDao.save(user);
            return orderDao.save(order);
        } else {
            throw new InvalidParameterException("User don't have enough money");
        }
    }
}