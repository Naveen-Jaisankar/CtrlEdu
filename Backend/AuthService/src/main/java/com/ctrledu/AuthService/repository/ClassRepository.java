package com.ctrledu.AuthService.repository;

import com.ctrledu.AuthService.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    @Query("SELECT c FROM ClassEntity c LEFT JOIN FETCH c.students LEFT JOIN FETCH c.modules WHERE c.org.orgId = :orgId")
    List<ClassEntity> findByOrgIdWithStudentsAndModules(@Param("orgId") Long orgId);

    @Query("SELECT c FROM ClassEntity c WHERE c.classId = :classId")
    Optional<ClassEntity> findById(@Param("classId") Long classId);

    void deleteById(Long classId);

}


