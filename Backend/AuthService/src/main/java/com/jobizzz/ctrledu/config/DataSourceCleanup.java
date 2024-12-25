package com.jobizzz.ctrledu.config;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;

@Component
public class DataSourceCleanup {

    private final DataSource dataSource;

    public DataSourceCleanup(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PreDestroy
    public void closeDataSource() {
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
        }
    }
}
