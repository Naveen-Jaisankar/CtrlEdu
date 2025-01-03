package com.ctrledu.MasterService.repository;
import com.ctrledu.AuthService.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MasterUserRepository extends JpaRepository<UserEntity, Long> {

    // Find user by email (useful for login authentication)
    UserEntity findByUserEmail(String userEmail);

    // Find all users for a given organization ID
    List<UserEntity> findByOrgId_OrgId(Long orgId);

    // Find all users with a specific role in a given organization
    @Query("SELECT u FROM UserEntity u WHERE u.orgId.orgId = :orgId AND u.userRole = :role")
    List<UserEntity> findByOrgIdAndRole(Long orgId, String role);

    // Custom query to find teachers by organization ID
    @Query("SELECT u FROM UserEntity u WHERE u.orgId.orgId = :orgId AND u.userRole = 'teacher'")
    List<UserEntity> findTeachersByOrgId(Long orgId);

    // Custom query to find students by organization ID
    @Query("SELECT u FROM UserEntity u WHERE u.orgId.orgId = :orgId AND u.userRole = 'student'")
    List<UserEntity> findStudentsByOrgId(Long orgId);

    @Query("SELECT u FROM UserEntity u WHERE u.userId = :userId")
    Optional<UserEntity> getUserById(@Param("userId") Long userId);

}
