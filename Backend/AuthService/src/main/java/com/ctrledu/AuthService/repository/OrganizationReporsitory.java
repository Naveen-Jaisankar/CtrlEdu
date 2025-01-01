package com.ctrledu.AuthService.repository;

import com.ctrledu.AuthService.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationReporsitory extends JpaRepository<OrganizationEntity,Long> {
}
