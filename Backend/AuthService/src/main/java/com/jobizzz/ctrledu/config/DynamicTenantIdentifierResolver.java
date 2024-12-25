package com.jobizzz.ctrledu.config;


import com.jobizzz.ctrledu.dto.ThreadContext;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class DynamicTenantIdentifierResolver implements CurrentTenantIdentifierResolver {
    @Override
    public String resolveCurrentTenantIdentifier() {
        String schemaName = ThreadContext.getThreadContext();
        System.out.println("Resolving Tenant Identifier: " + schemaName);
        return schemaName != null ? schemaName : "public";
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
