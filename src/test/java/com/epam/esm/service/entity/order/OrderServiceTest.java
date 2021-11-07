package com.epam.esm.service.entity.order;

import com.epam.esm.dao.order.OrderDao;
import com.epam.esm.dao.user.UserDao;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.entity.certificate.CertificateService;
import com.epam.esm.service.entity.user.UserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.security.InvalidParameterException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {
    @Mock
    OrderDao orderDao;
    @Mock
    UserDao userDao;
    @Mock
    UserService userService;
    private OrderService service;
    @Mock
    private Order order;
    private User user;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        service = new OrderService(orderDao, userDao, userService);
        user = new User();
        user.setId(1l);
        user.setPurse(34);
    }

    @Test
    public void addOrderWithoutExceptions() {
        long userPurseBefore = user.getPurse();
        doNothing().when(order).setTimeOfPurchase();
        doNothing().when(order).setPrice();
        when(order.getUserId()).thenReturn(1l);
        when(userService.read(1l)).thenReturn(user);
        when(order.getPrice()).thenReturn(10l);
        when(userDao.save(any())).thenReturn(null);
        when(orderDao.save(any())).thenReturn(new Order());
        service.addOrder(order);
        assertTrue(user.getPurse() < userPurseBefore);
    }

    @Test
    public void addOrderWithExceptions() {
        doNothing().when(order).setTimeOfPurchase();
        doNothing().when(order).setPrice();
        when(order.getUserId()).thenReturn(1l);
        when(userService.read(1l)).thenReturn(user);
        when(order.getPrice()).thenReturn(100l);
        expectedException.expect(InvalidParameterException.class);
        service.addOrder(order);
    }
}