package com.epam.esm.service.entity.order;

import com.epam.esm.dao.giftCertificate.impl.CertificateDao;
import com.epam.esm.dao.order.impl.OrderDao;
import com.epam.esm.dao.user.impl.UserDao;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.entity.user.UserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    OrderDao orderDao;
    @Mock
    UserDao userDao;
    @Mock
    UserService userService;
    @Mock
    CertificateDao certificateDao;
    private OrderService service;
    @Mock
    private Order order;
    private User user;
    private Certificate certificate;

    @Before
    public void setUp() throws Exception {
        service = new OrderService(orderDao, userDao, userService, certificateDao);
        user = new User();
        user.setId(1l);
        user.setPurse(34);
        certificate = new Certificate();
        certificate.setId(1l);
        certificate.setPrice(1);
    }

    @Test
    public void addOrderWithoutExceptions() {
        HashSet<Certificate> certificates = new HashSet<>();
        certificates.add(new Certificate());
        long userPurseBefore = user.getPurse();
        doNothing().when(order).setTimeOfPurchase();
        doNothing().when(order).setPrice();
        when(order.getUserId()).thenReturn(1l);
        when(order.getCertificates()).thenReturn(certificates);
        when(userService.read(1l)).thenReturn(user);
        when(order.getPrice()).thenReturn(10l);
        when(certificateDao.read(1l)).thenReturn(Optional.of(certificate));
        doNothing().when(userDao).create(user);
//     when(userDao.create(any());).thenReturn(null);
        doNothing().when(order).setCertificates(any());
        when(orderDao.create(any())).thenReturn(new Order());
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