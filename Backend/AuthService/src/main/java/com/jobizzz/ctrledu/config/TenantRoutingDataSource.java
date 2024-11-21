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

    private final Map<String, DataSource> dataSourcesPerDatabase = new HashMap<>();

    @Override
    protected Object determineCurrentLookupKey() {
        // Fetch the database name from ThreadContext for the current tenant
        String databaseName = ThreadContext.getDatabaseName();
        System.out.println("Using database: " + databaseName);
        return databaseName;
    }

    @Override
    public void afterPropertiesSet() {
        // Sets the target data sources map with all database connections
        super.setTargetDataSources((Map) dataSourcesPerDatabase);
        super.afterPropertiesSet();
    }

    public void addDataSource(String databaseName, DataSource dataSource) {
        // Adds the data source for a specific database
        dataSourcesPerDatabase.put(databaseName, dataSource);
        super.setTargetDataSources((Map) dataSourcesPerDatabase);
        super.afterPropertiesSet();
    }

    /**
     * Sets the search path to the tenant's schema.
     *
     * @param schemaName The schema name to set as the search path.
     */
    public void setSchema(String schemaName) {
        DataSource dataSource = dataSourcesPerDatabase.get(determineCurrentLookupKey());

        if (dataSource != null) {
            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {
                // Set the search_path to the correct schema for the tenant
                statement.execute("SET search_path TO " + schemaName);
                System.out.println("Search path set to schema: " + schemaName);
            } catch (SQLException e) {
                throw new RuntimeException("Error setting schema for tenant: " + schemaName, e);
            }
        } else {
            throw new IllegalStateException("Data source not found for database: " + determineCurrentLookupKey());
        }
    }
}
