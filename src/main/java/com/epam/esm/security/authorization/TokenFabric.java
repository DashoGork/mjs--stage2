package com.epam.esm.security.authorization;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.epam.esm.enums.Roles;
import com.epam.esm.enums.Secret;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

public class TokenFabric {

    public static String accessToken(HttpServletRequest request,
                                     Authentication authResult) {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(Secret.SECRET.name().toLowerCase().getBytes());
        String accessToken = JWT.create().withSubject(String.valueOf(user.getUsername()))
                .withExpiresAt(new Date(System.currentTimeMillis()
                        + 10 * 60 * 1000))
                .withIssuer(request.getRequestURI().toString())
                .withClaim(Roles.ROLE.name(),
                        user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        return accessToken;
    }

    public static String refreshToken(HttpServletRequest request,
                                      Authentication authResult) {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(Secret.SECRET.name().toLowerCase().getBytes());
        String refreshToken =
                JWT.create().withSubject(String.valueOf(user.getUsername()))
                        .withExpiresAt(new Date(System.currentTimeMillis()
                                + 30 * 60 * 1000))
                        .withIssuer(request.getRequestURI().toString())
                        .sign(algorithm);
        return refreshToken;
    }
}
