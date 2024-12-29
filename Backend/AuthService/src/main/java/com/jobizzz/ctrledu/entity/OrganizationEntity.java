package com.jobizzz.ctrledu.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CE_ORGANIZATIONS")
@Getter
@Setter
public class OrganizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CE_ORG_ID")
    private Long orgId;

    @Column(name = "CE_ORG_NAME", nullable = false)
    private String orgName;

    @Column(name = "CE_SUPER_ADMIN_USER_ID", nullable = true)
    private Long superAdminId;

    // Constructors
    public OrganizationEntity() {}

    public OrganizationEntity(String orgName, Long superAdminId) {
        this.orgName = orgName;
        this.superAdminId = superAdminId;
    }
}
