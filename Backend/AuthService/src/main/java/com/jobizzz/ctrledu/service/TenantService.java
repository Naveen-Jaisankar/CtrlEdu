package com.jobizzz.ctrledu.service;

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
    private DataSource dataSource;

    public Tenant registerTenant(String organizationName){
        Tenant tenant = new Tenant();
        try{
            String schemaName = "ce_" + organizationName.toLowerCase().replace(" ", "_");

            tenant.setOrgName(organizationName);
            tenant.setSchemaName(schemaName);

            tenantRepository.save(tenant);

            createSchema(schemaName);

        }catch (Exception e){
            System.err.println("Exception while registering tenant for the organization : " + organizationName);
        }
        return tenant;
    }

    private void createSchema(String schemaName){
        try(Connection connection = dataSource.getConnection()){
            Statement statement = connection.createStatement();
            statement.execute("CREATE SCHEMA IF NOT EXISTS " + schemaName);
        }catch (Exception e){
            throw new RuntimeException("Error creating schema", e);
        }
    }

    //TODO : Can me moved to core
    private void prepopulateTables(String schemaName){
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .schemas(schemaName)
                .locations("db/tenants") // Directory for tenant-specific scripts
                .load();
        flyway.migrate();
    }

}
