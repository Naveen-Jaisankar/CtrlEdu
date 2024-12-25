package com.jobizzz.ctrledu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Organization name (null for teacher/student)

    private String firstName; // First name
    private String lastName;  // Last name

    @Column(unique = true, nullable = false)
    private String email; // Email for login

    private String role; // Role: super-admin, teacher, student

    private String uniqueCode; // Unique code (used for teacher/student registration)

    private boolean used; // Whether the unique code has been used
    // Constructors
    public UserEntity() {}

    public UserEntity(String name, String firstName, String lastName, String email, String role, String uniqueCode, boolean used) {
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.uniqueCode = uniqueCode;
        this.used = used;
    }

}
