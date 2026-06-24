package com.minerva.infrastructure.persistence.repository;

import com.minerva.infrastructure.persistence.entity.UserActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserActionRepository extends JpaRepository<UserActionEntity, String> {
}
