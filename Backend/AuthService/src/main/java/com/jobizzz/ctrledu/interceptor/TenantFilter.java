package com.jobizzz.ctrledu.interceptor;

import com.jobizzz.ctrledu.dto.ThreadContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TenantFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String schemaName = request.getHeader("X-Tenant-Id");

        if (schemaName != null && !schemaName.isEmpty()) {
            ThreadContext.setThreadContext(schemaName);
            System.out.println("Tenant schema set to: " + schemaName);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            ThreadContext.clear();
        }
    }
}
