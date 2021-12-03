package com.epam.esm.service.entity.user;

import com.epam.esm.dao.user.UserDaoI;
import com.epam.esm.dao.user.impl.UserDao;
import com.epam.esm.exceptions.BaseNotFoundException;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.Role;
import com.epam.esm.model.entity.User;
import com.epam.esm.service.entity.PaginationCalcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.*;

@Service
public class UserService implements UserServiceI, PaginationCalcService,
        UserDetailsService {
    private UserDaoI userDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDao userDao, BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
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

    @Transactional
    @Override
    public User create(User user) {
        try {
            read(user.getName());
            throw new InvalidParameterException("wrong name");
        } catch (UsernameNotFoundException e) {
            Role role = new Role();
            role.setName("USER");
            role.setId(2);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(role);
            userDao.create(user);
            return userDao.read(user.getName()).get();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = read(name);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), authorities);
    }

    public User read(String name) {
        Optional<User> user = userDao.read(name);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User wasn't" +
                    " found. name =" + name);
        } else {
            return user.get();
        }
    }
}