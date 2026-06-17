package com.mireiagonzalez.urbanito.category;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueCategoryRepository extends JpaRepository<IssueCategory, UUID> {
    boolean existsByName(String name);
}
