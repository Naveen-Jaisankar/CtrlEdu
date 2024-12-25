package com.jobizzz.ctrledu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class TenantDataSourceInitializer {
    @Value("${spring.datasource.default.username}")
    private String defaultDbUsername;

    @Value("${spring.datasource.default.password}")
    private String defaultDbPassword;

    private final TenantRoutingDataSource tenantRoutingDataSource;
    private final AtomicInteger dbIndex = new AtomicInteger(0);
    private final Map<String, DataSource> dataSources = new ConcurrentHashMap<>();
    private final List<String> availableDatabases;

    @Autowired
    public TenantDataSourceInitializer(TenantRoutingDataSource tenantRoutingDataSource, Environment environment) {
        this.tenantRoutingDataSource = tenantRoutingDataSource;

        // Retrieve and parse the "databases" property
        String databasesProperty = environment.getProperty("databases");
        if (databasesProperty != null) {
            this.availableDatabases = Arrays.asList(databasesProperty.split(","));
        } else {
            throw new IllegalStateException("Property 'databases' not configured in application properties.");
        }

        initializeTenantDataSources();
    }

    private void initializeTenantDataSources() {
        availableDatabases.forEach(dbName -> {
            DataSource dataSource = DataSourceBuilder.create()
                    .url("jdbc:postgresql://localhost:5432/" + dbName.trim())
                    .username("postgres")
                    .password("Raghul_2002")
                    .build();
            tenantRoutingDataSource.addDataSource(dbName, dataSource);
            dataSources.put(dbName, dataSource); // Store in map for retrieval
        });
    }

    // Method to retrieve a DataSource by database name
    public DataSource getDataSource(String databaseName) {
        return dataSources.get(databaseName);
    }

    public String assignDatabaseForTenant(String tenantId) {
        String selectedDatabase = availableDatabases.get(dbIndex.getAndIncrement() % availableDatabases.size());
        // You may store the selected database in the tenant record in your database
        return selectedDatabase;
    }
}
