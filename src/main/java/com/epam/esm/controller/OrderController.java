package com.epam.esm.controller;


import com.epam.esm.dao.order.OrderDao;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.entity.Order;
import com.epam.esm.service.dto.tag.TagDtoService;
import com.epam.esm.service.dto.tag.TagDtoServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private OrderDao dao;

    @Autowired
    public OrderController(OrderDao dao) {
        this.dao = dao;
    }

    @GetMapping
    public List<Order> getAll() {
        return dao.findAll();
    }

}
