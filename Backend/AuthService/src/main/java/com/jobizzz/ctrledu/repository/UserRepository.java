package com.jobizzz.ctrledu.repository;

import com.jobizzz.ctrledu.entity.OrganizationEntity;
import com.jobizzz.ctrledu.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Find a user by their email
    Optional<UserEntity> findByUserEmail(String userEmail);

    Optional<UserEntity> findByUniqueCode(String uniqueCode);

    // Find all users belonging to the same organization by orgId (Long)
    @Query("SELECT u FROM UserEntity u WHERE u.orgId.orgId = :orgId")
    List<UserEntity> findByOrgId(@Param("orgId") Long orgId);

    @Query("SELECT u FROM UserEntity u WHERE u.orgId.orgId = :orgId AND u.userRole <> 'super-admin'")
    List<UserEntity> findByOrgIdExcludingSuperAdmin(@Param("orgId") Long orgId);
}
