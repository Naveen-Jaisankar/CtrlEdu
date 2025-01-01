package com.ctrledu.AuthService.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CE_USERS") // Specify the table name
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CE_USER_ID") // Specify the column name
    private Long userId;

    @Column(name = "CE_USER_FIRST_NAME") // Specify the column name
    private String userFirstName;

    @Column(name = "CE_USER_LAST_NAME") // Specify the column name
    private String userLastName;

    @Column(name = "CE_USER_EMAIL", unique = true, nullable = false) // Specify the column name
    private String userEmail;

    @Column(name = "CE_USER_ROLE") // Specify the column name
    private String userRole;

    @Column(name = "CE_UNIQUE_CODE") // Specify the column name
    private String uniqueCode;

    @Column(name = "CE_IS_CODE_USED") // Specify the column name
    private boolean isCodeUsed;

    @ManyToOne // Define the relationship as Many-to-One
    @JoinColumn(name = "CE_ORG_ID", referencedColumnName = "CE_ORG_ID") // Foreign key reference
    private OrganizationEntity orgId; // Reference to the OrganizationEntity

    // Constructors
    public UserEntity() {}

    public UserEntity(String userFirstName, String userLastName, String userEmail, String userRole, String uniqueCode, boolean isCodeUsed, OrganizationEntity orgId) {
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userRole = userRole;
        this.uniqueCode = uniqueCode;
        this.isCodeUsed = isCodeUsed;
        this.orgId = orgId;
    }
}
