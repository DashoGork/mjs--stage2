package com.epam.esm.controller;

import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.service.dto.user.UserDtoService;
import com.epam.esm.service.dto.user.UserDtoServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserDtoServiceI service;
    private final Link bestUserLink =
            linkTo(methodOn(UserController.class).getBest()).withRel("GET " +
                    "best user");

    @Autowired
    public UserController(UserDtoService service) {
        this.service = service;
    }

    @GetMapping
    public CollectionModel<UserDto> showAll(@RequestParam("page") int page,
                                            @RequestParam("size") int size) {
        final List<UserDto> users = service.findPaginated(page, size);
        users.stream().forEach((userDto -> setLinks(userDto)));
        users.stream().forEach((userDto -> userDto.getOrders().stream().forEach((orderDto -> setOrderLinks(orderDto)))));
        users.stream().forEach((userDto -> userDto.getOrders().stream().forEach((orderDto -> setCertificateLinks(orderDto)))));
        Link link =
                linkTo(WebMvcLinkBuilder.methodOn(UserController.class).showAll(page, size)).withSelfRel();
        CollectionModel<UserDto> result = CollectionModel.of(users
                , link, bestUserLink);
        return result;
    }

    @GetMapping("/{id}")
    public UserDto getUserWithOrders(
            @PathVariable("id") long id) {
        UserDto userDto = service.read(id);
        userDto = setLinks(userDto);
        userDto.getOrders().stream().forEach((orderDto -> setOrderLinks(orderDto)));
        userDto.getOrders().stream().forEach((orderDto -> setCertificateLinks(orderDto)));
        return setLinks(userDto);
    }

    @GetMapping("/{id}/orders")
    public List<OrderDto> getUserOrders(
            @PathVariable("id") long id) {
        List<OrderDto> orders = service.readOrdersByUserId(id);
        orders.stream().forEach((orderDto -> setOrderLinks(orderDto)));
        orders.stream().forEach((orderDto -> setCertificateLinks(orderDto)));
        return orders;
    }

    @GetMapping("/best")
    public UserDto getBest() {
        UserDto userDto = service.bestUser();
        setLinks(userDto);
        userDto.getOrders().stream().forEach((orderDto -> setOrderLinks(orderDto)));
        userDto.getOrders().stream().forEach((orderDto -> setCertificateLinks(orderDto)));
        return userDto;
    }


    private UserDto setLinks(UserDto userDto) {
        String userId = String.valueOf(userDto.getId());
        Link selfLink = linkTo(UserController.class).slash(userId)
                .withSelfRel();
        Link ordersLink =
                linkTo(methodOn(UserController.class).getUserOrders(userDto.getId())).withRel("GET " +
                        "orders");
        Link allLink =
                linkTo(WebMvcLinkBuilder.methodOn(UserController.class).showAll(1, 1)).withSelfRel();
        userDto.add(selfLink, ordersLink, bestUserLink, allLink);
        return userDto;
    }

    private OrderDto setOrderLinks(OrderDto orderDto) {
        orderDto.add(linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getUserWithOrders(orderDto.getUserId())).withSelfRel());
        return orderDto;
    }

    private OrderDto setCertificateLinks(OrderDto orderDto) {
        orderDto.getCertificates().stream().forEach(
                certificate -> certificate.add(linkTo((CertificateController.class)).withRel("POST create"),
                        linkTo(CertificateController.class).slash("id").withRel(
                                "DELETE by id"),
                        linkTo((CertificateController.class)).slash("id").withRel(
                                "PATCH by id"),
                        linkTo((CertificateController.class)).slash("id").withRel(
                                "GET by id"),
                        linkTo((CertificateController.class)).withRel(
                                "GET all")));
        return orderDto;
    }

}