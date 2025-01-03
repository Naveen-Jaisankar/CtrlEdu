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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public boolean isCodeUsed() {
        return isCodeUsed;
    }

    public void setCodeUsed(boolean codeUsed) {
        isCodeUsed = codeUsed;
    }

    public OrganizationEntity getOrgId() {
        return orgId;
    }

    public void setOrgId(OrganizationEntity orgId) {
        this.orgId = orgId;
    }
}
