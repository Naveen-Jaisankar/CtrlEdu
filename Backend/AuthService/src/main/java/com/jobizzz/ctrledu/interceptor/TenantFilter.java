package com.jobizzz.ctrledu.interceptor;

import com.jobizzz.ctrledu.config.TenantRoutingDataSource;
import com.jobizzz.ctrledu.dto.ThreadContext;
import com.jobizzz.ctrledu.entity.OrganizationEntity;
import com.jobizzz.ctrledu.repository.OrganizationRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class TenantFilter extends OncePerRequestFilter {

    private final OrganizationRepository organizationRepository;
    private final TenantRoutingDataSource tenantRoutingDataSource;

    @Autowired
    public TenantFilter(OrganizationRepository organizationRepository, TenantRoutingDataSource tenantRoutingDataSource) {
        this.organizationRepository = organizationRepository;
        this.tenantRoutingDataSource = tenantRoutingDataSource;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String orgName = request.getHeader("X-Tenant-Id");

        if (orgName != null && !orgName.isEmpty()) {
            Optional<OrganizationEntity> tenantOpt = organizationRepository.findBySchemaName(orgName);

            if (tenantOpt.isPresent()) {
                OrganizationEntity organizationEntity = tenantOpt.get();

                // Set the context for tenant schema and database
                ThreadContext.setThreadContext(organizationEntity.getSchemaName(), organizationEntity.getDatabaseName());

                // Set the database and schema search path
                tenantRoutingDataSource.setSchema(organizationEntity.getSchemaName());

                System.out.println("Tenant database and schema set for: " + orgName);
            } else {
                System.err.println("Tenant with schema name " + orgName + " not found.");
            }
        } else {
            System.err.println("No 'X-Tenant-Id' header provided in the request.");
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Clear the tenant context after the request is processed
            ThreadContext.clear();
        }
    }
}
