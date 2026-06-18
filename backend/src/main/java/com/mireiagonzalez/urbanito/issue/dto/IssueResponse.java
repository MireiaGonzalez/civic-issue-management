package com.mireiagonzalez.urbanito.issue.dto;

import java.time.Instant;
import java.util.UUID;

import com.mireiagonzalez.urbanito.issue.IssuePriority;
import com.mireiagonzalez.urbanito.issue.IssueStatus;

public record IssueResponse(UUID id, UUID reporterId, String reporterName,
        UUID categoryId, String categoryName, String title, String description, String location,
        IssueStatus status, IssuePriority priority, Instant createdAt, Instant updatedAt,
        Instant resolvedAt) {

}
