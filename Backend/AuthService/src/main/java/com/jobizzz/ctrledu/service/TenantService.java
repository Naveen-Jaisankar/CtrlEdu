package com.jobizzz.ctrledu.service;

import com.jobizzz.ctrledu.config.TenantDataSourceInitializer;
import com.jobizzz.ctrledu.config.TenantRoutingDataSource;
import com.jobizzz.ctrledu.entity.Tenant;
import com.jobizzz.ctrledu.repository.TenantRepository;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Service
public class TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private TenantDataSourceInitializer tenantDataSourceInitializer;

    public Tenant registerTenant(String organizationName){
        Tenant tenant = new Tenant();
        try{
            String schemaName = "ce_" + organizationName.toLowerCase().replace(" ","_");
            String assignedDatabase = tenantDataSourceInitializer.assignDatabaseForTenant(schemaName);

            tenant.setOrgName(organizationName);
            tenant.setSchemaName(schemaName);
            tenant.setDatabaseName(assignedDatabase);
            tenantRepository.save(tenant);

            createSchema(schemaName, assignedDatabase);
            prepopulateTables(schemaName, assignedDatabase);

        }catch(Exception e){
            System.err.println("Exception while registering tenant for the organization: " + organizationName);
            e.printStackTrace();
        }
        return tenant;
    }

    private void createSchema(String schemaName, String databaseName) {
        DataSource dataSource = tenantDataSourceInitializer.getDataSource(databaseName);
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE SCHEMA IF NOT EXISTS " + schemaName);
            System.out.println("Created schema: " + schemaName + " in database: " + databaseName);
        } catch (Exception e) {
            throw new RuntimeException("Error creating schema", e);
        }
    }

    private void prepopulateTables(String schemaName, String databaseName) {
        DataSource dataSource = tenantDataSourceInitializer.getDataSource(databaseName);
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .schemas(schemaName)
                .locations("db/tenants")
                .load();
        flyway.migrate();
        System.out.println("Prepopulated tables for schema: " + schemaName);
    }
}
