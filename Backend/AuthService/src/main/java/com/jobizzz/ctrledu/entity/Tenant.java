package com.jobizzz.ctrledu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "CE_TENANTS")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CE_TENANT_ID")
    private Long tenantId;

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "org_id_seq")
    @SequenceGenerator(name = "org_id_seq", sequenceName = "organization_id_seq", allocationSize = 1)
    @Column(name = "CE_ORGANIZATION_ID", unique = true, nullable = false)
    private Integer orgId;

    @Column(name = "CE_ORGANIZATION_NAME")
    private String orgName;

    @Column(name = "CE_SCHEMA_NAME", unique = true)
    private String schemaName;

    @Column(name = "CE_CREATED_AT")
    private LocalDateTime createdAt;
}
