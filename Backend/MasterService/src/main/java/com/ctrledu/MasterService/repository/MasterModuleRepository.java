package com.ctrledu.MasterService.repository;

import com.ctrledu.AuthService.entity.ClassEntity;
import com.ctrledu.AuthService.entity.ModuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MasterModuleRepository extends JpaRepository<ModuleEntity, Long> {

    // Find all modules for a specific organization
    @Query("SELECT m FROM ModuleEntity m WHERE m.orgId.orgId = :orgId")
    List<ModuleEntity> findModulesByOrgId(Long orgId);


    @Query(value = "SELECT c.* FROM classes c " +
            "JOIN class_modules cm ON c.class_id = cm.class_id " +
            "JOIN ce_modules m ON cm.module_id = m.module_id " +
            "WHERE m.teacher_id = :teacherId",
            nativeQuery = true)

List<ClassEntity> findByTeacherId(@Param("teacherId") Long teacherId);
    //    @Query("SELECT m FROM ModuleEntity m WHERE m.teacher.userId = :teacherId")
//    List<ModuleEntity> findByTeacherId(@Param("teacherId") Long teacherID);

    // Find all modules assigned to a specific teacher
    @Query("SELECT m FROM ModuleEntity m WHERE m.teacher.userId = :teacherId")
    List<ModuleEntity> findModulesByTeacherId(Long teacherId);

    // Find a module by its unique module code
    ModuleEntity findByModuleCode(String moduleCode);

    // Find modules that are unassigned (without a teacher)
    @Query("SELECT m FROM ModuleEntity m WHERE m.teacher IS NULL AND m.orgId.orgId = :orgId")
    List<ModuleEntity> findUnassignedModulesByOrgId(Long orgId);
}
