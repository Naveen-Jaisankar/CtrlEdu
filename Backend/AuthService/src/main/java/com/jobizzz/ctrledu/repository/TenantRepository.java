package com.jobizzz.ctrledu.repository;

import com.jobizzz.ctrledu.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant,Long> {

    Optional<Tenant> findBySchemaName(String schemaName);

    Optional<Tenant> findByOrgId(Integer orgId);

    List<Tenant> findAll();

}
