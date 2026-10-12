package com.atharva.educationcrm.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RoleAwareAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        boolean adminMode = "true".equalsIgnoreCase(request.getParameter("admin"));
        String query = adminMode ? "?admin=true&error=true" : "?error=true";
        response.sendRedirect(request.getContextPath() + "/auth/login" + query);
    }
}
