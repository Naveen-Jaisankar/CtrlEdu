package com.jobizzz.ctrledu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class TenantDataSourceInitializer {

    private final JdbcTemplate jdbcTemplate;
    private final TenantRoutingDataSource tenantRoutingDataSource;

    @Value("${spring.datasource.tenant.url}")
    private String tenantDbUrlPrefix;

    @Value("${spring.datasource.tenant.username}")
    private String tenantDbUsername;

    @Value("${spring.datasource.tenant.password}")
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

            DataSource tenantDataSource = DataSourceBuilder.create()
                    .url(tenantDbUrlPrefix + schemaName)
                    .username(tenantDbUsername)
                    .password(tenantDbPassword)
                    .build();

            tenantRoutingDataSource.addDataSource(schemaName, tenantDataSource);
        });
    }
}

