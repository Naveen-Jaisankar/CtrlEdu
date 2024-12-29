package com.jobizzz.ctrledu.repository;

import com.jobizzz.ctrledu.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserEmail(String email);
    Optional<UserEntity> findByUniqueCode(String uniqueCode);
}
