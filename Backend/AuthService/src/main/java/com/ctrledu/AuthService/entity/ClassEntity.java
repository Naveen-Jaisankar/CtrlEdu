package com.ctrledu.AuthService.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "classes")
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classId; // Unique ID for the class

    @Column(nullable = false)
    private String className; // Name of the class

    @Column(name = "num_students", nullable = false)
    private Integer numberOfStudents;

    @ManyToOne
    @JoinColumn(name = "org_id", nullable = false)
    private OrganizationEntity org; // Organization the class belongs to

    @ManyToMany
    @JoinTable(
            name = "class_modules",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id")
    )
    private Set<ModuleEntity> modules = new HashSet<>(); // Modules associated with the class

    @ManyToMany
    @JoinTable(
            name = "class_students",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<UserEntity> students = new HashSet<>(); // Students associated with the class
}
