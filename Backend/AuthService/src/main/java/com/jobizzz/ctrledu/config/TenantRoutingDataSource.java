package com.jobizzz.ctrledu.config;

import com.jobizzz.ctrledu.dto.ThreadContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TenantRoutingDataSource extends AbstractRoutingDataSource {

    // Thread-safe map to store data sources for each database
    private final Map<String, DataSource> dataSourcesPerDatabase = new ConcurrentHashMap<>();

    @Override
    protected Object determineCurrentLookupKey() {
        // Fetch the database name from ThreadContext
        String databaseName = ThreadContext.getDatabaseName();
        System.out.println("Using database: " + databaseName);
        return databaseName;
    }

    @Override
    protected DataSource determineTargetDataSource() {
        String databaseName = ThreadContext.getDatabaseName();
        if (databaseName == null || databaseName.isEmpty()) {
            System.out.println("No database name set in ThreadContext, Falling back to default database");
            databaseName = "common_db";
        }

        // Fetch or throw an exception if DataSource not found
        DataSource dataSource = dataSourcesPerDatabase.get(databaseName);
        if (dataSource == null) {
            System.out.println("No DataSource found for database: " + databaseName);
        }

        return dataSource;
    }

    @Override
    public void afterPropertiesSet() {
        // Initialize parent class with all available data sources
        super.setTargetDataSources((Map) dataSourcesPerDatabase);
        super.afterPropertiesSet();
    }

    public void addDataSource(String databaseName, DataSource dataSource) {
        // Add a new DataSource for a specific database
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
        String currentDatabase = (String) determineCurrentLookupKey();

        DataSource dataSource = dataSourcesPerDatabase.get(currentDatabase);
        if (dataSource != null) {
            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {
                // Set the search path to the tenant's schema
                statement.execute("SET search_path TO " + schemaName);
                System.out.println("Search path set to schema: " + schemaName);
            } catch (SQLException e) {
                throw new RuntimeException("Error setting schema for tenant: " + schemaName, e);
            }
        } else {
            throw new IllegalStateException("Data source not found for database: " + currentDatabase);
        }
    }
}
