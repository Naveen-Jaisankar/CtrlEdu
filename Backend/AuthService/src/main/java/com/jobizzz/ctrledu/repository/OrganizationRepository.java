package com.jobizzz.ctrledu.repository;

import com.jobizzz.ctrledu.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationReporsitory extends JpaRepository<OrganizationEntity,Long> {
}
