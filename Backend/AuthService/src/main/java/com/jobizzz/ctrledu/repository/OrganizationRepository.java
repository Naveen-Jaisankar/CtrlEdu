package com.jobizzz.ctrledu.repository;

import com.jobizzz.ctrledu.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationEntity,Long> {

    Optional<OrganizationEntity> findByOrgId(Long orgId);
}
