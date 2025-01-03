package com.ctrledu.AuthService.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "ce_modules")
public class ModuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moduleId;

    @Column(nullable = false, unique = true)
    private String moduleCode;

    @Column(nullable = false)
    private String moduleName;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "CE_USER_ID", nullable = false)
    private UserEntity teacher;

    @ManyToOne
    @JoinColumn(name = "CE_ORG_ID", referencedColumnName = "CE_ORG_ID", nullable = false)
    private OrganizationEntity orgId;

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public UserEntity getTeacher() {
        return teacher;
    }

    public void setTeacher(UserEntity teacher) {
        this.teacher = teacher;
    }

    public OrganizationEntity getOrgId() {
        return orgId;
    }

    public void setOrgId(OrganizationEntity orgId) {
        this.orgId = orgId;
    }
}
