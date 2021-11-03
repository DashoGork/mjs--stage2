package com.epam.esm.controller;

import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.dto.TagDto;
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

@RestController
@RequestMapping("/users")
public class UserController {

    private UserDtoServiceI service;

    @Autowired
    public UserController(UserDtoService service) {
        this.service = service;
    }

    @GetMapping
    public CollectionModel<UserDto> showAll(@RequestParam("page") int page,
                                            @RequestParam("size") int size) {
        final List<UserDto> users = service.findPaginated(page, size);
        for (final UserDto user : users) {
            String userId = String.valueOf(user.getId());
            Link selfLink = linkTo(UserController.class).slash(userId)
                    .withSelfRel();
            user.add(selfLink);
        }
        Link link =
                linkTo(WebMvcLinkBuilder.methodOn(UserController.class).showAll(page, size)).withSelfRel();
        CollectionModel<UserDto> result = CollectionModel.of(users
                , link);
        return result;
    }

    @GetMapping("/{id}")
    public UserDto showUserWithOrders(
            @PathVariable("id") int id) {
        return service.read(id);
    }

    @GetMapping("/{id}/orders")
    public List<OrderDto> showUserOrders(
            @PathVariable("id") int id) {
        return service.readOrdersByUserId(id);
    }

    @GetMapping("/best")
    public UserDto showBest() {
        return service.bestUser();
    }

}