package com.epam.esm.exceptions;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exc) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(403);
        PrintWriter out = response.getWriter();
        out.print("{\"code\": \"40003\",\n" +
                "    \"message\": \"Access denied\"}");
        out.flush();
    }
}