package com.jobizzz.ctrledu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class TenantDataSourceInitializer {

    private final JdbcTemplate jdbcTemplate;
    private final TenantRoutingDataSource tenantRoutingDataSource;

    @Value("${spring.datasource.tenant.url:jdbc:postgresql://localhost:5432/}")
    private String tenantDbUrlPrefix;

    @Value("${spring.datasource.tenant.username:postgres}")
    private String tenantDbUsername;

    @Value("${spring.datasource.tenant.password:password}")
    private String tenantDbPassword;

    @Autowired
    public TenantDataSourceInitializer(JdbcTemplate jdbcTemplate, TenantRoutingDataSource tenantRoutingDataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.tenantRoutingDataSource = tenantRoutingDataSource;

        initializeTenantDataSources();
    }

    private void initializeTenantDataSources() {
        String sql = "SELECT CE_SCHEMA_NAME FROM CE_TENANTS";
        jdbcTemplate.query(sql, (rs) -> {
            String schemaName = rs.getString("CE_SCHEMA_NAME");
            System.out.println("Initializing DataSource for schema: " + schemaName);

            DataSource tenantDataSource = DataSourceBuilder.create()
                    .url("jdbc:postgresql://localhost:5432/common_db")
                    .username("postgres")
                    .password("Raghul_2002")
                    .build();

            // Add data source to routing and set schema
            tenantRoutingDataSource.addDataSource(schemaName, tenantDataSource);
            try (Connection connection = tenantDataSource.getConnection()) {
                TenantRoutingDataSource.setSchema(connection, schemaName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
