package com.epam.esm.controller;

import com.epam.esm.model.entity.User;
import com.epam.esm.service.entity.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<User> showAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public User showUserWithOrders(
            @PathVariable("id") int id) {
        return service.findAll().get(id);
    }
}
