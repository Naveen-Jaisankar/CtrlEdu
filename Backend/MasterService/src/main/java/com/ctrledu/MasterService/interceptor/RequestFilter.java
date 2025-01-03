package com.ctrledu.MasterService.interceptor;

import com.ctrledu.AuthService.dto.ThreadContext;
import com.ctrledu.AuthService.repository.UserRepository;
import com.ctrledu.MasterService.model.Thread;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class RequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Retrieve the authentication object
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
                Jwt jwt = (Jwt) authentication.getPrincipal();

                String email = jwt.getClaimAsString("email");

                // Set in ThreadContext
                Thread.setEmail(email);
            }

            filterChain.doFilter(request, response);

        } finally {
            // Clear ThreadLocal to avoid memory leaks
            Thread.clear();
        }
    }

}
