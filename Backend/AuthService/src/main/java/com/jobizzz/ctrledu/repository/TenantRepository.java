package com.jobizzz.ctrledu.repository;

import com.jobizzz.ctrledu.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {


    Optional<Tenant> findBySchemaName(String schemaName);

    Optional<Tenant> findByOrgId(Long orgId);

    Optional<Tenant> findByOrgName(String orgName);
}
