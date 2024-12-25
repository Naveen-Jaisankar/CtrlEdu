package com.jobizzz.ctrledu.service;

import com.jobizzz.ctrledu.config.TenantDataSourceInitializer;
import com.jobizzz.ctrledu.config.TenantRoutingDataSource;
import com.jobizzz.ctrledu.dto.ThreadContext;
import com.jobizzz.ctrledu.entity.OrganizationEntity;
import com.jobizzz.ctrledu.entity.UserEntity;
import com.jobizzz.ctrledu.repository.OrganizationRepository;
import com.jobizzz.ctrledu.repository.UserRepository;
import com.jobizzz.ctrledu.request.SignupRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Service
public class AuthService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private TenantDataSourceInitializer tenantDataSourceInitializer;

    @Autowired
    private TenantRoutingDataSource tenantRoutingDataSource;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;


    public OrganizationEntity register(SignupRequest signupRequest){
        OrganizationEntity organizationEntity = new OrganizationEntity();
        try{
            //Creating a entry in CE_ORGANIZATION table
            organizationEntity = addEntryToOrganizationTable(signupRequest);

            //Create a schema in the assigned database
            createSchema(organizationEntity.getSchemaName(),organizationEntity.getDatabaseName());

            //Prepopulate tables for the organization
            prePopulateTables(organizationEntity.getSchemaName(),organizationEntity.getDatabaseName());

            //Now Clear the existing thread context, so that we can switch to this user's schema and the super admin to the user entity table
            ThreadContext.clear();
            ThreadContext.setThreadContext(organizationEntity.getSchemaName(), organizationEntity.getDatabaseName());
            tenantRoutingDataSource.setSchema(organizationEntity.getSchemaName());
            //Add the super admin to the user entity table of that particular application
            UserEntity user = addEntryToUserTable(signupRequest,organizationEntity.getOrgId().longValue());

        }catch (Exception e){
            System.err.println("Exception while registering user for organization : " + signupRequest.getOrganizationName());
            e.printStackTrace();
        }
        return organizationEntity;
    }

    @Transactional
    public UserEntity addEntryToUserTable(SignupRequest signupRequest, Long orgId) {
        UserEntity user = new UserEntity();
        try {

            System.out.println("Switching to Database: " + ThreadContext.getDatabaseName());
            System.out.println("Switching to Schema: " + ThreadContext.getThreadContext());

            // Dynamically switch the schema in the current database connection
            entityManager.unwrap(Session.class).doWork(connection -> {
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("SET SCHEMA '" + ThreadContext.getThreadContext() + "'");
                }
            });

            // Populate user details
            user.setUserFirstName(signupRequest.getFirstName());
            user.setUserLastName(signupRequest.getLastName());
            user.setUserEmail(signupRequest.getEmail());
            user.setOrgId(orgId);
            user.setUserRole("super-admin");
            user.setIsActivated("false");

            // Save user entity
            userRepository.save(user);
        } catch (Exception e) {
            System.err.println("Error while adding user: " + e.getMessage());
            e.printStackTrace();
        }
        return user;
    }





    public OrganizationEntity addEntryToOrganizationTable(SignupRequest signupRequest){
        OrganizationEntity organizationEntity = new OrganizationEntity();
        try{
            String organizationName = signupRequest.getOrganizationName();
            String schemaName = "ce_" + organizationName.toLowerCase().replace(" ","_");
            String assignedDatabase = tenantDataSourceInitializer.assignDatabaseForTenant(schemaName);

            organizationEntity.setOrgName(organizationName);
            organizationEntity.setSchemaName(schemaName);
            organizationEntity.setDatabaseName(assignedDatabase);
            organizationRepository.save(organizationEntity);

        }catch (Exception e){
            System.err.println("Exception while adding data to CE_ORGANIZATION table for organization : " + signupRequest.getOrganizationName());
            e.printStackTrace();
        }
        return organizationEntity;
    }


    private void createSchema(String schemaName, String databaseName) {
        DataSource dataSource = tenantDataSourceInitializer.getDataSource(databaseName);
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE SCHEMA IF NOT EXISTS " + schemaName);
            System.out.println("Created schema: " + schemaName + " in database: " + databaseName);
        } catch (Exception e) {
            throw new RuntimeException("Error while creating schema for schemaName : " + schemaName + " databaseName : " + databaseName, e);
        }
    }

    private void prePopulateTables(String schemaName, String databaseName) {
        try{
            DataSource dataSource = tenantDataSourceInitializer.getDataSource(databaseName);
            Flyway flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .schemas(schemaName)
                    .locations("db/tenants")
                    .load();
            flyway.migrate();
            System.out.println("Successfully populated tables for schema: " + schemaName);
        }catch (Exception e){
            System.err.println("Exception while populating tables, schema : " + schemaName + ", database : " + databaseName);
        }

    }
}
