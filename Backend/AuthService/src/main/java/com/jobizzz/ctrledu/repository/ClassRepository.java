package com.jobizzz.ctrledu.repository;

import com.jobizzz.ctrledu.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    List<ClassEntity> findByOrgOrgId(Long orgId);
}

