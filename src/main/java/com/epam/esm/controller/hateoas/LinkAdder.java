package com.epam.esm.controller.hateoas;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.model.dto.*;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public interface LinkAdder {
    default void addLinks(TagDto tagDto) {
        Link selfLink = linkTo(TagController.class).slash(tagDto.getId())
                .withRel("GET by id");
        Link deleteLink = linkTo(TagController.class).slash(tagDto.getId())
                .withRel("DELETE by id");
        Link link =
                linkTo(WebMvcLinkBuilder.methodOn(TagController.class).getAll(1, 1)).withRel("GET all");
        Link createLink = linkTo(TagController.class)
                .withRel("POST create");
        tagDto.add(selfLink, deleteLink, link, createLink);
    }

    default void addLinks(OrderDto orderDto) {
        orderDto.add(linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getUserWithOrders(orderDto.getUserId())).withSelfRel());
    }

    default void addLinks(CertificateDto certificateDto) {
        certificateDto.add(linkTo((CertificateController.class)).withRel(
                        "POST create"),
                linkTo(CertificateController.class).slash(certificateDto.getId()).withRel(
                        "DELETE by id"),
                linkTo((CertificateController.class)).slash(certificateDto.getId()).withRel(
                        "PATCH by id"),
                linkTo((CertificateController.class)).slash(certificateDto.getId()).withRel(
                        "GET by id"),
                linkTo((CertificateController.class)).withRel(
                        "GET all"));
    }

    default void addLinks(UserDto userDto) {
        String userId = String.valueOf(userDto.getId());
        Link selfLink = linkTo(UserController.class).slash(userId)
                .withSelfRel();
        Link ordersLink =
                linkTo(methodOn(UserController.class).getUserOrders(userDto.getId())).withRel("GET " +
                        "orders");
        Link allLink =
                linkTo(WebMvcLinkBuilder.methodOn(UserController.class).showAll(1, 1)).withRel("GET all");
        Link bestUserLink =
                linkTo(methodOn(UserController.class).getBest()).withRel("GET " +
                        "best user");
        userDto.add(selfLink, ordersLink, bestUserLink, allLink);
    }
}
