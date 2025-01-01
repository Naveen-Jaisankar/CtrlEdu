package com.jobizzz.ctrledu.repository;

import com.jobizzz.ctrledu.entity.ModuleEntity;
import com.jobizzz.ctrledu.entity.OrganizationEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.data.jpa.repository.JpaRepository;
 // Adjust the package if necessary
import com.jobizzz.ctrledu.response.ModuleResponse;
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
    SELECT new com.jobizzz.ctrledu.response.ModuleResponse(
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
