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

    @Query(value = "SELECT c.* FROM classes c " +
            "JOIN class_modules cm ON c.class_id = cm.class_id " +
            "JOIN ce_modules m ON cm.module_id = m.module_id " +
            "WHERE m.teacher_id = :teacherId",
            nativeQuery = true)

List<ClassEntity> findByTeacherId(@Param("teacherId") Long teacherId);

}
