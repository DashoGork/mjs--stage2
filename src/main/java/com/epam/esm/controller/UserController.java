package com.epam.esm.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.epam.esm.controller.hateoas.LinkAdder;
import com.epam.esm.enums.Secret;
import com.epam.esm.enums.Tokens;
import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.dto.UserDto;
import com.epam.esm.service.dto.user.UserDtoService;
import com.epam.esm.service.dto.user.UserDtoServiceI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import java.security.InvalidParameterException;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("/users")
public class UserController implements LinkAdder {

    private UserDtoServiceI service;

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
            @Min(1) @PathVariable("id") long id) {
        if (isAllowed(id)) {
            UserDto userDto = service.read(id);
            setLinks(userDto);
            return userDto;
        } else throw new AccessDeniedException("Access denied");
    }

    @GetMapping("/{id}/orders")
    public List<OrderDto> getUserOrders(
            @Min(1) @PathVariable("id") long id) {
        if (isAllowed(id)) {
            List<OrderDto> orders = service.readOrdersByUserId(id);
            orders.stream().forEach((orderDto -> setOrderLinks(orderDto)));
            return orders;
        } else throw new AccessDeniedException("Access denied");
    }

    @PostMapping
    public UserDto saveUser(@RequestBody UserDto userDto) {
        return service.create(userDto);
    }

    @GetMapping("refresh")
    public void refresh(HttpServletRequest request,
                        HttpServletResponse response) {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(Secret.SECRET.name().toLowerCase().getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getSubject();
                UserDto userDto = service.read(username);
                List<SimpleGrantedAuthority> authorityList =
                        new ArrayList<>();
                authorityList.add(new SimpleGrantedAuthority(userDto.getRole().getName()));
                String accessToken =
                        JWT.create().withSubject(String.valueOf(userDto.getName()))
                                .withExpiresAt(new Date(System.currentTimeMillis()
                                        + 10 * 60 * 1000)).withClaim("role",
                                        userDto.getRole().getName())
                                .withIssuer(request.getRequestURI().toString())
                                .sign(algorithm);

                String refreshToken =
                        JWT.create().withSubject(String.valueOf(userDto.getName()))
                                .withExpiresAt(new Date(System.currentTimeMillis()
                                        + 30 * 60 * 1000))
                                .withIssuer(request.getRequestURI().toString())
                                .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put(Tokens.ACCESS_TOKEN.name().toLowerCase(), accessToken);
                tokens.put(Tokens.REFRESH_TOKEN.name().toLowerCase(), refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception e) {
                throw new InvalidParameterException("invalid refresh token");
            }
        } else {
            throw new InvalidParameterException("invalid refresh token");
        }
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

    private boolean isAllowed(long id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        UserDto user = service.read(username);
        if (user.getRole().getName().equals("ADMIN")) {
            return true;
        } else {
            return user.getId() == id;
        }
    }
}