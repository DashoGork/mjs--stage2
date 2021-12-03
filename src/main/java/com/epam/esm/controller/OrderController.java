package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.LinkAdder;
import com.epam.esm.enums.Roles;
import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.service.dto.order.OrderDtoService;
import com.epam.esm.service.dto.order.OrderDtoServiceI;
import com.epam.esm.service.dto.user.UserDtoService;
import com.epam.esm.service.dto.user.UserDtoServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/orders")
public class OrderController implements LinkAdder {
    private final OrderDtoServiceI orderService;
    private final UserDtoServiceI userDtoService;

    @Autowired
    public OrderController(OrderDtoService orderService,
                           UserDtoService userDtoServiceI) {
        this.orderService = orderService;
        this.userDtoService = userDtoServiceI;
    }

    @PostMapping
    public OrderDto create(@RequestBody OrderDto order) {
        if (isAllowed(order)) {
            OrderDto createdOrder = orderService.create(order);
            addLinks(createdOrder);
            return createdOrder;
        } else {
            throw new AccessDeniedException("");
        }
    }

    private boolean isAllowed(OrderDto orderDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        UserDto user = userDtoService.read(username);
        if (user.getRole().getName().equals(Roles.ADMIN.name())) {
            return true;
        } else if (((Long) (orderDto.getUserId()) == 0) | (orderDto.getUserId() == user.getId())) {
            orderDto.setUserId(user.getId());
            return true;
        } else {
            return false;
        }
    }
}