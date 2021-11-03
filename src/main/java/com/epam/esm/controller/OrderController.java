package com.epam.esm.controller;

import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.service.dto.order.OrderDtoService;
import com.epam.esm.service.dto.order.OrderDtoServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderDtoServiceI orderService;

    @Autowired
    public OrderController(OrderDtoService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderDto create(@RequestBody OrderDto order) {
        return orderService.create(order);
    }

}
