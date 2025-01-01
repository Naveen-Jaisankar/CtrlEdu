package com.jobizzz.ctrledu.entity;

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


}
