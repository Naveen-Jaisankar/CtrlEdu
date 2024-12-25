package com.jobizzz.ctrledu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name ="CE_USERS")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CE_USER_ID", unique = true, nullable = false)
    private Long userId;

    @Column(name = "CE_ORGANIZATION_ID", unique = true, nullable = false)
    private Long orgId;

    @Column(name = "CE_USER_FIRSTNAME", nullable = false)
    private String userFirstName;

    @Column(name = "CE_USER_LASTNAME",nullable = false)
    private String userLastName;

    @Column(name = "CE_USER_EMAIL", unique = true)
    private String userEmail;

    @Column(name = "CE_USER_ROLE", nullable = false)
    private String userRole;

    @Column(name = "CE_USER_UNIQUE_CODE", unique = true)
    private String userUniqueCode;

    @Column(name = "CE_IS_ACTIVATED")
    private String isActivated;



}
