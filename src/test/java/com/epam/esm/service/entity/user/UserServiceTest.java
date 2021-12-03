package com.epam.esm.service.entity.user;

import com.epam.esm.dao.user.impl.UserDao;
import com.epam.esm.exceptions.BaseNotFoundException;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private UserDao userDao;
    @Mock
    BCryptPasswordEncoder passwordEncoder;


    private UserService service;
    private User firstUser;
    private User secondUser;
    private List<User> listOfAll;

    @Before
    public void setUp() throws Exception {
        service = new UserService(userDao, passwordEncoder);
        firstUser = new User();
        firstUser.setPurse(12);
        firstUser.setId(12);
        firstUser.setName("name");
        secondUser = new User();
        secondUser.setPurse(12);
        secondUser.setId(12);
        secondUser.setName("23");
        secondUser.setOrders(new ArrayList<>());
        listOfAll = new ArrayList<>();
        listOfAll.add(firstUser);
        listOfAll.add(secondUser);
    }

    @Test
    public void read() {
        when(userDao.read()).thenReturn(listOfAll);
        assertTrue(service.read().equals(listOfAll));
    }

    @Test
    public void testReadNotExisting() {
        Optional<User> expected = Optional.ofNullable(null);
        when(userDao.read(1l)).thenReturn(expected);
        expectedException.expect(BaseNotFoundException.class);
        service.read(1);
    }

    @Test
    public void testReadById() {
        Optional<User> expected = Optional.of(secondUser);
        when(userDao.read(1l)).thenReturn(expected);
        User actual = service.read(1);
        assertTrue(actual.equals(expected.get()));
    }

    @Test
    public void readOrdersByIdUser() {
        Optional<User> expected = Optional.ofNullable(secondUser);
        when(userDao.read(1l)).thenReturn(expected);
        List<Order> orders = secondUser.getOrders();
        assertTrue(service.readOrdersByIdUser(1l).equals(orders));
    }

    @Test
    public void findPaginated() {
        when(userDao.read()).thenReturn(listOfAll);
        when(userDao.read(0, 1)).thenReturn(listOfAll);
        assertTrue(service.findPaginated(1, 1).size() == 2);
    }
}