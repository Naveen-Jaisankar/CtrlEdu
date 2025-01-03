package com.ctrledu.AuthService.repository;

import com.ctrledu.AuthService.entity.ModuleEntity;
import com.ctrledu.AuthService.entity.OrganizationEntity;
import com.ctrledu.AuthService.entity.UserEntity;
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

    @Query("SELECT u FROM UserEntity u WHERE u.orgId.orgId = :orgId AND u.userRole = 'teacher'")
    List<UserEntity> findTeachersByOrgId(@Param("orgId") Long orgId);

    @Query("""
    SELECT u FROM UserEntity u 
    WHERE u.userRole = 'teacher' 
    AND u.orgId = :orgEntity 
    AND (:moduleId IS NULL OR u.userId NOT IN (
        SELECT m.teacher.userId FROM ModuleEntity m WHERE m.moduleId != :moduleId
    ))
""")
    List<UserEntity> findAvailableTeachersByOrgAndCurrentTeacher(
            @Param("orgEntity") OrganizationEntity orgEntity,
            @Param("moduleId") Long moduleId);

    @Query("""
    SELECT u FROM UserEntity u 
    WHERE u.userRole = 'teacher' 
    AND u.orgId = :orgEntity
    AND u.userId NOT IN (SELECT m.teacher.userId FROM ModuleEntity m)
""")
    List<UserEntity> findAvailableTeachersForAdd(@Param("orgEntity") OrganizationEntity orgEntity);

    @Query("""
    SELECT u FROM UserEntity u 
    WHERE u.userRole = 'teacher' 
    AND u.orgId = :orgEntity 
    AND u.userId NOT IN (
        SELECT m.teacher.userId FROM ModuleEntity m
    )
""")
    List<UserEntity> findAvailableTeachersByOrg(@Param("orgEntity") OrganizationEntity orgEntity);

    @Query("""
    SELECT u FROM UserEntity u 
    WHERE u.userRole = 'teacher' 
    AND u.orgId = :orgId
""")
    List<UserEntity> findAllTeachersByOrgId(@Param("orgId") Long orgId);

    @Query("SELECT u.orgId FROM UserEntity u WHERE u.userEmail = :email")
    Optional<Long> findOrgIdByEmail(@Param("email") String email);

    @Query("""
    SELECT u FROM UserEntity u 
    WHERE u.userRole = 'teacher' 
    AND u.orgId = :orgEntity 
    AND u.userId NOT IN (
        SELECT m.teacher.userId FROM ModuleEntity m
    )
""")
    List<UserEntity> findAvailableTeachersByOrgEntity(@Param("orgEntity") OrganizationEntity orgEntity);

    @Query("""
    SELECT u FROM UserEntity u 
    WHERE u.userRole = 'teacher' 
    AND u.orgId = :orgEntity 
    AND (
        u.userId = :currentTeacherId 
        OR u.userId NOT IN (
            SELECT m.teacher.userId FROM ModuleEntity m WHERE m.teacher IS NOT NULL
        )
    )
""")
    List<UserEntity> findAllTeachersForEdit(
            @Param("orgEntity") OrganizationEntity orgEntity,
            @Param("currentTeacherId") Long currentTeacherId
    );
    @Query("SELECT m FROM ModuleEntity m WHERE m.orgId = :orgId")
    List<ModuleEntity> findModulesByOrgId(@Param("orgId") Long orgId);
    @Query("SELECT u FROM UserEntity u WHERE u.orgId.orgId = :orgId AND u.userRole = 'student'")
    List<UserEntity> findStudentsByOrgId(@Param("orgId") Long orgId);

    @Query("""
SELECT u FROM UserEntity u 
WHERE u.orgId.orgId = :orgId 
AND u.userRole = 'student' 
AND NOT EXISTS (
    SELECT 1 FROM ClassEntity c 
    JOIN c.students s 
    WHERE s.userId = u.userId
)
""")
    List<UserEntity> findUnassignedStudentsByOrgId(@Param("orgId") Long orgId);




}
