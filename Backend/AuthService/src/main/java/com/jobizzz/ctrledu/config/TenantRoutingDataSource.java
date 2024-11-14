package com.jobizzz.ctrledu.config;

import com.jobizzz.ctrledu.dto.ThreadContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class TenantRoutingDataSource extends AbstractRoutingDataSource {

    private final Map<Object, Object> resolvedDataSources = new HashMap<>();

    @Override
    protected Object determineCurrentLookupKey() {
        String tenantId = ThreadContext.getThreadContext();
        System.out.println("Using tenant schema: " + tenantId);
        return tenantId;
    }

    @Override
    public void afterPropertiesSet() {
        super.setTargetDataSources(resolvedDataSources);
        super.afterPropertiesSet();
    }

    public void addDataSource(String schemaName, DataSource dataSource) {
        resolvedDataSources.put(schemaName, dataSource);
        super.setTargetDataSources(resolvedDataSources);
        super.afterPropertiesSet();
    }

    // Method to set schema on each connection
    public static void setSchema(Connection connection, String schemaName) {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("SET search_path TO " + schemaName);
            System.out.println("Schema set to: " + schemaName);
        } catch (SQLException e) {
            throw new RuntimeException("Error setting schema for tenant", e);
        }
    }
}
