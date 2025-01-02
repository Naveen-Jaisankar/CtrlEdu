package com.ctrledu.AuthService.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@Entity
@Table(name = "classes")
public class ClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId;

    @Column(nullable = false)
    private String className;

    @Column(nullable = false)
    private Integer numStudents;

    @ManyToOne
    @JoinColumn(name = "org_id", nullable = false)
    private OrganizationEntity org;

    @ManyToMany
    @JoinTable(
            name = "class_modules",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id")
    )
    private List<ModuleEntity> modules;

}
