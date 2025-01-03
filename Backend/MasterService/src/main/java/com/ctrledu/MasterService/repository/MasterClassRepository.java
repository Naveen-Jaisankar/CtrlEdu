package com.ctrledu.MasterService.repository;

import com.ctrledu.AuthService.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MasterClassRepository extends JpaRepository<ClassEntity, Long> {

    @Query(value = "SELECT c.* FROM classes c " +
            "JOIN class_students cs ON c.class_id = cs.class_id " +
            "WHERE cs.student_id = :studentId",
            nativeQuery = true)
    List<ClassEntity> findClassesForStudent(@Param("studentId") Long studentId);
}
