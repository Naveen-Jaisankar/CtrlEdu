package com.ctrledu.AuthService.repository;

import com.ctrledu.AuthService.entity.ModuleEntity;
import com.ctrledu.AuthService.entity.OrganizationEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ctrledu.AuthService.response.ModuleResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<ModuleEntity, Long> {

    @Query("SELECT COUNT(m) > 0 FROM ModuleEntity m WHERE m.teacher.userId = :teacherId")
    boolean existsByTeacherUserId(Long teacherId);

    @Query("""
        SELECT COUNT(m) > 0 FROM ModuleEntity m
        WHERE m.teacher.userId = :teacherId AND m.moduleId != :moduleId
    """)
    boolean isTeacherAssignedToAnotherModule(Long teacherId, Long moduleId);

    @Query("""
    SELECT new com.ctrledu.AuthService.response.ModuleResponse(
        m.moduleId,
        m.moduleCode,
        m.moduleName,
        CONCAT(u.userFirstName, ' ', u.userLastName),
        u.userId
    )
    FROM ModuleEntity m
    LEFT JOIN UserEntity u ON m.teacher.userId = u.userId
""")
    List<ModuleResponse> findAllModulesWithTeacherName();

    @Query("""
    SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END
    FROM ModuleEntity m
    WHERE m.teacher.userId = :teacherId
""")
    boolean isTeacherAssigned(@Param("teacherId") Long teacherId);
    @Query("SELECT m FROM ModuleEntity m WHERE m.teacher.orgId = :organization")
    List<ModuleEntity> findModulesByOrganization(@Param("organization") OrganizationEntity organization);

    @Query("""
    SELECT m FROM ModuleEntity m 
    WHERE m.orgId.orgId = :orgId
""")
    List<ModuleEntity> findByOrgId(@Param("orgId") Long orgId);

}
