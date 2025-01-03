package com.ctrledu.MasterService.repository;

import com.ctrledu.AuthService.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MasterClassRepository extends JpaRepository<ClassEntity, Long> {

    // Find all classes for a specific organization
    @Query("SELECT c FROM ClassEntity c WHERE c.org.orgId = :orgId")
    List<ClassEntity> findClassesByOrgId(Long orgId);

    // Find classes that include a specific module
    @Query("SELECT c FROM ClassEntity c JOIN c.modules m WHERE m.moduleId = :moduleId")
    List<ClassEntity> findClassesByModuleId(Long moduleId);

    // Find classes that have a specific name within an organization
    @Query("SELECT c FROM ClassEntity c WHERE c.className = :className AND c.org.orgId = :orgId")
    List<ClassEntity> findClassesByClassNameAndOrgId(String className, Long orgId);

    // Find all classes with more than a specific number of students
    @Query("SELECT c FROM ClassEntity c WHERE c.numStudents > :numStudents")
    List<ClassEntity> findClassesByNumStudentsGreaterThan(Integer numStudents);


    @Query(value = "SELECT c.* FROM classes c " +
            "JOIN class_students cs ON c.class_id = cs.class_id " +
            "WHERE cs.student_id = :studentId",
            nativeQuery = true)
    List<ClassEntity> findClassesForStudent(@Param("studentId") Long studentId);
}
