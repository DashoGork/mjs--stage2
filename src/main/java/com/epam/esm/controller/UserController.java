package com.epam.esm.controller;

import com.epam.esm.controller.hateoas.LinkAdder;
import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.service.dto.user.UserDtoService;
import com.epam.esm.service.dto.user.UserDtoServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Validated
@RestController
@RequestMapping("/users")
public class UserController implements LinkAdder {

    private UserDtoServiceI service;
    private final Link bestUserLink =
            linkTo(methodOn(UserController.class).getBest()).withRel("GET " +
                    "best user");

    @Autowired
    public UserController(UserDtoService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserDto> showAll(@Min(1) @RequestParam("page") int page,
                                 @Min(1) @RequestParam("size") int size) {
        final List<UserDto> users = service.findPaginated(page, size);
        users.stream().forEach((userDto -> setLinks(userDto)));
        return users;
    }

    @GetMapping("/{id}")
    public UserDto getUserWithOrders(
            @PathVariable("id") long id) {
        UserDto userDto = service.read(id);
        setLinks(userDto);
        return userDto;
    }

    @GetMapping("/{id}/orders")
    public List<OrderDto> getUserOrders(
            @PathVariable("id") long id) {
        List<OrderDto> orders = service.readOrdersByUserId(id);
        orders.stream().forEach((orderDto -> setOrderLinks(orderDto)));
        return orders;
    }

    @GetMapping("/best")
    public UserDto getBest() {
        UserDto userDto = service.bestUser();
        setLinks(userDto);
        return userDto;
    }


    private void setLinks(UserDto userDto) {
        addLinks(userDto);
        userDto.getOrders().stream().
                forEach((orderDto -> setOrderLinks(orderDto)));
    }

    private void setOrderLinks(OrderDto orderDto) {
        addLinks(orderDto);
        orderDto.getCertificates().stream().
                forEach((certificateDto -> addLinks(certificateDto)));
        orderDto.getCertificates().stream().
                forEach((certificateDto -> certificateDto.getTags().stream().
                        forEach((tagDto -> addLinks(tagDto)))));
    }
}