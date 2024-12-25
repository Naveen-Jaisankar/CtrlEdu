package com.jobizzz.ctrledu.config;

import com.jobizzz.ctrledu.dto.ThreadContext;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class TenantSchemaNamingStrategy extends PhysicalNamingStrategyStandardImpl implements PhysicalNamingStrategy {

    private static final long serialVersionUID = 1L;

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        // Get the current tenant identifier (schema name)
        String tenant = ThreadContext.getThreadContext();
        if (tenant != null && !tenant.isEmpty()) {
            // Prepend the tenant schema name to the table name
            String prefixedName = tenant + "." + name.getText();
            return Identifier.toIdentifier(prefixedName, name.isQuoted());
        }
        // Default behavior if no tenant is set
        return super.toPhysicalTableName(name, context);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        // Use default behavior for column names
        return super.toPhysicalColumnName(name, context);
    }

    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment context) {
        // Use default behavior for catalog names
        return super.toPhysicalCatalogName(name, context);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment context) {
        // Use default behavior for schema names
        return super.toPhysicalSchemaName(name, context);
    }
}
