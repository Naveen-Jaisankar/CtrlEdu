package com.jobizzz.ctrledu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.default.url}")
    private String defaultDbUrl;

    @Value("${spring.datasource.default.username}")
    private String defaultDbUsername;

    @Value("${spring.datasource.default.password}")
    private String defaultDbPassword;

    @Bean
    public DataSource dataSource() {
        TenantRoutingDataSource tenantRoutingDataSource = new TenantRoutingDataSource();

        System.out.println("Default data source : " + defaultDbUrl );
        // Default DataSource for common database
        DataSource defaultDataSource = DataSourceBuilder.create()
                .url(defaultDbUrl)
                .username(defaultDbUsername)
                .password(defaultDbPassword)
                .build();

        tenantRoutingDataSource.setDefaultTargetDataSource(defaultDataSource);
        return tenantRoutingDataSource;
    }
}
