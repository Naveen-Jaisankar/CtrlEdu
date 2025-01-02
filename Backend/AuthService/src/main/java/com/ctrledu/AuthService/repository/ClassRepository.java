package com.ctrledu.AuthService.repository;

import com.ctrledu.AuthService.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    @Query("SELECT c FROM ClassEntity c LEFT JOIN FETCH c.students WHERE c.org.orgId = :orgId")
    List<ClassEntity> findByOrgIdWithStudents(@Param("orgId") Long orgId);

}


