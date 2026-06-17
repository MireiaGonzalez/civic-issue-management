package com.mireiagonzalez.urbanito.issue;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, UUID> {

}
