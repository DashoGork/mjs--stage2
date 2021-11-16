package com.epam.esm.service.entity.order;

import com.epam.esm.dao.giftCertificate.CertificateDaoI;
import com.epam.esm.dao.giftCertificate.impl.CertificateDao;
import com.epam.esm.dao.order.OrderDaoI;
import com.epam.esm.dao.order.impl.OrderDao;
import com.epam.esm.dao.user.UserDaoI;
import com.epam.esm.dao.user.impl.UserDao;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.entity.user.UserService;
import com.epam.esm.service.entity.user.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.stream.Collectors;

@Service
public class OrderService implements OrderServiceI {
    private OrderDaoI orderDao;
    private UserDaoI userDao;
    private UserServiceI userService;
    private CertificateDaoI certificateDao;

    @Autowired
    public OrderService(OrderDao orderDao,
                        UserDao userDao,
                        UserService userService,
                        CertificateDao certificateDao) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.userService = userService;
        this.certificateDao = certificateDao;
    }

    @Transactional
    public Order addOrder(Order order) {
        if (order.getCertificates().size() != 0) {
            order.setPrice();
            order.setTimeOfPurchase();

            User user = userService.read(order.getUserId());
            order.setCertificates(order.getCertificates().stream()
                    .map(certificate -> certificateDao.read(certificate.getId()).get())
                    .collect(Collectors.toSet()));
            if (user.getPurse() >= order.getPrice()) {
                user.setPurse(user.getPurse() - order.getPrice());
                userDao.create(user);
                return orderDao.create(order);
            } else {
                throw new InvalidParameterException("User don't have enough money");
            }
        } else {
            throw new InvalidParameterException("Empty certificate list");
        }
    }
}