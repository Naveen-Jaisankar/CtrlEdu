-- Create a sequence for CE_ORGANIZATION_ID with a starting value of 100000
CREATE SEQUENCE IF NOT EXISTS organization_id_seq
    START WITH 100000
    INCREMENT BY 1
    NO MAXVALUE;

-- Create the CE_TENANTS table with CE_ORGANIZATION_ID as the primary key
CREATE TABLE CE_TENANTS (
    CE_ORGANIZATION_ID INT PRIMARY KEY DEFAULT nextval('organization_id_seq') UNIQUE NOT NULL,
    CE_ORGANIZATION_NAME VARCHAR(255),
    CE_SCHEMA_NAME VARCHAR(255) UNIQUE,
    CE_DATABASE_NAME VARCHAR(255) NOT NULL,  -- New column to store the assigned database for each tenant
    CE_CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
